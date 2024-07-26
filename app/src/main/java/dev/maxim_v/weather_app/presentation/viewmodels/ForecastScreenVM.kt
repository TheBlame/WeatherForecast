package dev.maxim_v.weather_app.presentation.viewmodels

import android.util.Log
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
class ForecastScreenVM @Inject constructor(
    private val getFullForecastUseCase: GetFullForecastUseCase,
    private val getLocationWithGpsUseCase: GetLocationWithGpsUseCase
) : ViewModel() {

    var forecastScreenState by mutableStateOf<ForecastScreenState>(ForecastScreenState.Initial)
        private set

    private val _errorFlow = MutableSharedFlow<ForecastScreenError>()
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
            forecastScreenState = ForecastScreenState.Loading
            getLocation()
            getWeather()
        }
    }

    private suspend fun getLocation() {
        try {
            getLocationWithGpsUseCase()
        } catch (e: GpsException) {
            _errorFlow.emit(ForecastScreenError.GpsError)
        } catch (e: NetworkException) {
            _errorFlow.emit(ForecastScreenError.GpsAndNetworkError)
        }
    }

    private suspend fun getWeather() {
        getFullForecastUseCase()
            .onStart { forecastScreenState = ForecastScreenState.Loading }
            .catch {
                Log.d("worker", it.toString())
                if (it is NetworkException) _errorFlow.emit(ForecastScreenError.NetworkError)
                if (it is DatabaseException) {
                    Log.d("worker", it.message.toString())
                    forecastScreenState = ForecastScreenState.NoContent
                }
            }
            .collectLatest {
                forecastScreenState = ForecastScreenState.Content(it)
            }
    }

    fun onEvent(event: ForecastScreenEvent) {
        when (event) {
            ForecastScreenEvent.GetLocationWithGps -> getLocationWithGps()
            ForecastScreenEvent.Refresh -> viewModelScope.launch { getWeather() }
        }
    }
}
