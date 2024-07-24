package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationWithGeocodingUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(locationName: String): Flow<List<SearchedLocation>> {
        return repository.getLocationWithGeocoding(locationName)
    }
}