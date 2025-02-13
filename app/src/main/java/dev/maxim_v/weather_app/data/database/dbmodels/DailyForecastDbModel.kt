package dev.maxim_v.weather_app.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType

@Entity(tableName = "daily_forecast")
data class DailyForecastDbModel(
    val minTemp: Double,
    val maxTemp: Double,
    val weatherType: WeatherType,
    @PrimaryKey
    val timestamp: Long
)
