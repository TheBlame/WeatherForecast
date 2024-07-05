package dev.maxim_v.weather_app.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxim_v.weather_app.domain.entity.WeatherType

@Entity(tableName = "current_forecast")
data class CurrentForecastDbModel(
    val temp: Double,
    val weatherType: WeatherType,
    val timestamp: Long,
    @PrimaryKey
    val id: Int = 1
)
