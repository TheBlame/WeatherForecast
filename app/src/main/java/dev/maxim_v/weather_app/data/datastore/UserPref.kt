package dev.maxim_v.weather_app.data.datastore

import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType.AUTO
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.MS
import kotlinx.serialization.Serializable

@Serializable
data class UserPref(
    val tempUnit: TemperatureUnit = CELSIUS,
    val windSpeedUnit: WindSpeedUnit = MS,
    val theme: ThemeType = AUTO
)