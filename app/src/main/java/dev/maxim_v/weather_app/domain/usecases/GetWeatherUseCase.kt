package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(vararg weatherSample: WeatherSample): Flow<Map<WeatherSample, WeatherModel>> {
        return repository.getWeather(*weatherSample)
    }
}