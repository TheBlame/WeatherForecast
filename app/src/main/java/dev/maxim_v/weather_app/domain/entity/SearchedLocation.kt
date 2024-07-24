package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class SearchedLocation(
    val country: String,
    val city: String,
    val area: String,
    val latitude: Double,
    val longitude: Double
)