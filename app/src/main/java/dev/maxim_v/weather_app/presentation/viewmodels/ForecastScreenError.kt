package dev.maxim_v.weather_app.presentation.viewmodels

sealed class ForecastScreenError {
    data object NetworkError : ForecastScreenError()
    data object GpsError : ForecastScreenError()
    data object GpsAndNetworkError : ForecastScreenError()
}