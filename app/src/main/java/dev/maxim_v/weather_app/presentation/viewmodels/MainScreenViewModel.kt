package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.exceptions.DatabaseException
import dev.maxim_v.weather_app.domain.exceptions.GpsException
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import dev.maxim_v.weather_app.domain.usecases.GetFullForecastUseCase
import dev.maxim_v.weather_app.domain.usecases.GetLocationWithGpsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getFullForecastUseCase: GetFullForecastUseCase,
    private val getLocationWithGpsUseCase: GetLocationWithGpsUseCase
) : ViewModel() {

    var mainScreenState by mutableStateOf<MainScreenState>(MainScreenState.Initial)
        private set

    private val _errorFlow = MutableSharedFlow<MainScreenError>()
    val errorFlow = _errorFlow.asSharedFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch {
            getWeather()
        }
    }

    private fun getLocationWithGps() {
        job?.cancel()
        job = viewModelScope.launch {
            mainScreenState = MainScreenState.Loading
            getLocation()
            getWeather()
        }
    }

    private suspend fun getLocation() {
        try {
            getLocationWithGpsUseCase()
        } catch (e: GpsException) {
            _errorFlow.emit(MainScreenError.GpsError)
        } catch (e: NetworkException) {
            _errorFlow.emit(MainScreenError.GpsAndNetworkError)
        }
    }

    private suspend fun getWeather() {
        getFullForecastUseCase()
            .onStart { mainScreenState = MainScreenState.Loading }
            .catch {
                if (it is NetworkException) _errorFlow.emit(MainScreenError.NetworkError)
                if (it is DatabaseException) mainScreenState = MainScreenState.NoContent
            }
            .collectLatest {
                mainScreenState = MainScreenState.Content(it)
            }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.GetLocationWithGps -> getLocationWithGps()
            MainScreenEvent.Refresh -> viewModelScope.launch { getWeather() }
        }
    }
}
