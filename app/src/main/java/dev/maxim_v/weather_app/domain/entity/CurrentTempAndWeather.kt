package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class CurrentTempAndWeather(
    val temp: String,
    val weatherType: WeatherType
)
