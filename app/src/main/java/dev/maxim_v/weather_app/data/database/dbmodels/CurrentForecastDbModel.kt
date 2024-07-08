package dev.maxim_v.weather_app.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxim_v.weather_app.data.WeatherCode

@Entity(tableName = "current_forecast")
data class CurrentForecastDbModel(
    val temp: Double,
    val weatherCode: WeatherCode,
    val timestamp: Long,
    val location: String,
    @PrimaryKey
    val id: Int = 0
)
