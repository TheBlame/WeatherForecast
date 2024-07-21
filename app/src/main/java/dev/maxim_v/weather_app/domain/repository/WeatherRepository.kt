package dev.maxim_v.weather_app.domain.repository

import dev.maxim_v.weather_app.domain.entity.FullForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getFullForecast(): Flow<FullForecast>

    suspend fun getLocationWithGps()
}