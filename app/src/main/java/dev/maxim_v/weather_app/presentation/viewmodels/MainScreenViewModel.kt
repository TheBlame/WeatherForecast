package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
import dev.maxim_v.weather_app.domain.usecases.GetLocationWithGpsUseCase
import dev.maxim_v.weather_app.domain.usecases.GetWeatherUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLocationWithGpsUseCase: GetLocationWithGpsUseCase
) : ViewModel() {

    var mainScreenState by mutableStateOf<MainScreenState>(MainScreenState.Initial)
        private set

    private var job: Job? = null

    init {
        viewModelScope.launch {
            getWeather()
        }
    }

    private fun getLocationWithGps() {
        job?.cancel()
        job = viewModelScope.launch {
            getLocationWithGpsUseCase.invoke()
            getWeather()
        }
    }

    private suspend fun getWeather() {
        getWeatherUseCase(CURRENT, HOURLY, DAILY)
            .onStart { mainScreenState = MainScreenState.Loading }
            .collectLatest {
                mainScreenState = MainScreenState.Success(it)
            }
    }


    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.GetLocationWithGps -> getLocationWithGps()
        }
    }
}