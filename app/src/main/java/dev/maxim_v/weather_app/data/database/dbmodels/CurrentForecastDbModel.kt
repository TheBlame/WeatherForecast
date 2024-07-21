package dev.maxim_v.weather_app.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit

@Entity(tableName = "current_forecast")
data class CurrentForecastDbModel(
    val temp: Double,
    val apparentTemp: Double,
    val precipitation: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val temperatureUnit: TemperatureUnit,
    val windSpeedUnit: WindSpeedUnit,
    val weatherType: WeatherType,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey
    val id: Int = 0
)
