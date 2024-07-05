package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(private val repository: WeatherRepository) {

    operator fun invoke(vararg weatherSample: WeatherSample): Flow<List<WeatherModel>> {
        return repository.getWeather(*weatherSample)
    }
}