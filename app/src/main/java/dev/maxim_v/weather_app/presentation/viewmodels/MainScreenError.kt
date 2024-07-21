package dev.maxim_v.weather_app.presentation.viewmodels

sealed class MainScreenError {
    data object NetworkError : MainScreenError()
    data object GpsError : MainScreenError()
    data object GpsAndNetworkError : MainScreenError()
}