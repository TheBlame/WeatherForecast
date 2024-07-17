package dev.maxim_v.weather_app.data.datastore


import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnit
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnit
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnit.MS
import dev.maxim_v.weather_app.domain.entity.ThemeType
import dev.maxim_v.weather_app.domain.entity.ThemeType.AUTO

import kotlinx.serialization.Serializable

@Serializable
data class UserPref(
    val location: Location = Location(),
    val tempUnit: TemperatureUnit = CELSIUS,
    val windSpeedUnit: WindSpeedUnit = MS,
    val theme: ThemeType = AUTO
)

@Serializable
data class Location(
    val latitude: Double = 55.7522,
    val longitude: Double = 37.6156,
    val city: String = "Москва"
)
