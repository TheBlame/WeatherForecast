package dev.maxim_v.weather_app.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxim_v.weather_app.data.WeatherCode

@Entity(tableName = "hourly_forecast")
data class HourlyForecastDbModel(
    val temp: Double,
    val weatherCode: WeatherCode,
    @PrimaryKey
    val timestamp: Long
)
