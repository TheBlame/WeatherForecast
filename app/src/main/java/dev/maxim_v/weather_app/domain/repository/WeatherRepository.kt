package dev.maxim_v.weather_app.domain.repository

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeather(vararg weatherSample: WeatherSample): Flow<Map<WeatherSample, WeatherModel>>
}