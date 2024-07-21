package dev.maxim_v.weather_app.data.network.dto

import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.FAHRENHEIT

enum class Temperature2UnitDto(val unit: TemperatureUnit) {
    @SerializedName("°C")
    CELSIUS_DTO(CELSIUS),
    @SerializedName("°F")
    FAHRENHEIT_DTO(FAHRENHEIT)
}