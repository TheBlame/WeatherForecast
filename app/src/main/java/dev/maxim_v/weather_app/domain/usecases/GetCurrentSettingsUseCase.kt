package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentSettingsUseCase @Inject constructor(private val repository: WeatherRepository) {

    operator fun invoke(): Flow<UserSettings> {
        return repository.getCurrentSettings()
    }
}