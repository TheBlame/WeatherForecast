package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import dev.maxim_v.weather_app.domain.usecases.GetLocationWithGeocodingUseCase
import dev.maxim_v.weather_app.domain.usecases.SaveLocationFromSearchUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchScreenVM @Inject constructor(
    private val getLocationWithGeocodingUseCase: GetLocationWithGeocodingUseCase,
    private val saveLocationFromSearchUseCase: SaveLocationFromSearchUseCase
) : ViewModel() {

    var locationSearchScreenState by mutableStateOf<LocationSearchScreenState>(
        LocationSearchScreenState.Initial
    )
        private set

    private var job: Job? = null

    private fun searchLocation(locationName: String) {
        if (locationName.isBlank()) return
        job?.cancel()
        job = viewModelScope.launch {
            getLocationWithGeocodingUseCase(locationName)
                .onStart { locationSearchScreenState = LocationSearchScreenState.Loading }
                .catch {
                    locationSearchScreenState = if (it is NetworkException) {
                        LocationSearchScreenState.Error
                    } else {
                        LocationSearchScreenState.NoResult
                    }
                }
                .collectLatest {
                    locationSearchScreenState = if (it.isEmpty()) {
                        LocationSearchScreenState.NoResult
                    } else {
                        LocationSearchScreenState.Result(it)
                    }
                }
        }
    }

    private fun saveLocation(location: SearchedLocation) {
        viewModelScope.launch {
            saveLocationFromSearchUseCase(location)
        }
    }

    fun onEvent(event: LocationSearchScreenEvent) {
        when (event) {
            is LocationSearchScreenEvent.Search -> searchLocation(event.locationName)
            is LocationSearchScreenEvent.SaveLocation -> saveLocation(event.location)
        }
    }
}