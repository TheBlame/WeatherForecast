package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class GetLocationWithGpsUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke() {
        return repository.getLocationWithGps()
    }
}