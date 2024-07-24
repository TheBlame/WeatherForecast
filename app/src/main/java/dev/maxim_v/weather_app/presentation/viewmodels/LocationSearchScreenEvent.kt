package dev.maxim_v.weather_app.presentation.viewmodels

import dev.maxim_v.weather_app.domain.entity.SearchedLocation

sealed class LocationSearchScreenEvent {
    data class Search(val locationName: String) : LocationSearchScreenEvent()
    data class SaveLocation(val location: SearchedLocation) : LocationSearchScreenEvent()
}