package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveLocationFromSearchUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(location: SearchedLocation) {
        repository.saveLocation(location)
    }
}