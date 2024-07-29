package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class UpdateWidgetsDataUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(highPriority: Boolean = false) {
        repository.loadCurrentTempAndWeather(highPriority)
    }
}