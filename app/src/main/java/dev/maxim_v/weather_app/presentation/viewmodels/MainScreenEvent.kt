package dev.maxim_v.weather_app.presentation.viewmodels

sealed class MainScreenEvent {
    data object GetLocationWithGps : MainScreenEvent()
}