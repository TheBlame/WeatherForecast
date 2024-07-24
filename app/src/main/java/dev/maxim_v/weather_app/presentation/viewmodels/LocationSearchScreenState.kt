package dev.maxim_v.weather_app.presentation.viewmodels

import dev.maxim_v.weather_app.domain.entity.SearchedLocation

sealed class LocationSearchScreenState {
    data object Initial : LocationSearchScreenState()
    data object Loading : LocationSearchScreenState()
    data class Result(val data: List<SearchedLocation>) : LocationSearchScreenState()
    data object NoResult : LocationSearchScreenState()
    data object Error : LocationSearchScreenState()
}