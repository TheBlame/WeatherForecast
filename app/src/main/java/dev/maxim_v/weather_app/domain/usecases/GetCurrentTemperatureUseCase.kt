package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentTemperatureUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(): String {
        return repository.getCurrentTemp()
    }
}