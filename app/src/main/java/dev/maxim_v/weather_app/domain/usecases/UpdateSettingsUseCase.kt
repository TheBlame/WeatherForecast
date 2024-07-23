package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(newSettings: UserSettings) {
        repository.updateSettings(newSettings)
    }
}