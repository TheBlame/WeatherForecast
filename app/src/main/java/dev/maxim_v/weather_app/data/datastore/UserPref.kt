package dev.maxim_v.weather_app.data.datastore

import dev.maxim_v.weather_app.domain.entity.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.ThemeType
import dev.maxim_v.weather_app.domain.entity.ThemeType.AUTO
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit.MS
import kotlinx.serialization.Serializable

@Serializable
data class UserPref(
    val userLocation: UserLocation = UserLocation(),
    val tempUnit: TemperatureUnit = CELSIUS,
    val windSpeedUnit: WindSpeedUnit = MS,
    val theme: ThemeType = AUTO
)

@Serializable
data class UserLocation(
    val latitude: Double = 55.7522,
    val longitude: Double = 37.6156,
    val city: String = "Москва"
)
