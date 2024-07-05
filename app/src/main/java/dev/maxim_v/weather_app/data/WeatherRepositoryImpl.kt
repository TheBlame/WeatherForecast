package dev.maxim_v.weather_app.data

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl : WeatherRepository {
    override fun getWeather(vararg weatherSample: WeatherSample): Flow<List<WeatherModel>> {
        TODO("Not yet implemented")
    }
}