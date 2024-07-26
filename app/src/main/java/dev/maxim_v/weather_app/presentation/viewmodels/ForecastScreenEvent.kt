package dev.maxim_v.weather_app.presentation.viewmodels

sealed class ForecastScreenEvent {
    data object GetLocationWithGps : ForecastScreenEvent()
    data object Refresh : ForecastScreenEvent()
}