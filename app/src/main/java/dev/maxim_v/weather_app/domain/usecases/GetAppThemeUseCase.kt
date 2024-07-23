package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(private val repository: WeatherRepository) {

    operator fun invoke(): Flow<ThemeType> {
        return repository.getAppTheme()
    }
}