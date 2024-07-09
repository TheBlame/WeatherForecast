package dev.maxim_v.weather_app.presentation.viewmodels

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample

sealed class MainScreenState {

    data object Initial : MainScreenState()
    data object Loading : MainScreenState()
    data class Success(val data: Map<WeatherSample, WeatherModel>) : MainScreenState()
    data object Error : MainScreenState()
}