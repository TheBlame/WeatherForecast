package dev.maxim_v.weather_app.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserSavedLocation(
    val latitude: Double = 55.7522,
    val longitude: Double = 37.6156,
    val city: String = "Москва"
)
