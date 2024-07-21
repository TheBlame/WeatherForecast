package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.FullForecast
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFullForecastUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(): Flow<FullForecast> {
        return repository.getFullForecast()
    }
}