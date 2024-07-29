package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit

@Immutable
data class ForegroundWorkStatus(
    val enabled: Boolean,
    val location: String,
    val temperatureUnit: TemperatureUnit
)
