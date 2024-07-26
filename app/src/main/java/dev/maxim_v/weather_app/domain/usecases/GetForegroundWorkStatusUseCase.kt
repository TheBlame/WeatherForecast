package dev.maxim_v.weather_app.domain.usecases

import dev.maxim_v.weather_app.domain.entity.ForegroundWorkStatus
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForegroundWorkStatusUseCase @Inject constructor(private val repository: WeatherRepository) {

    operator fun invoke(): Flow<ForegroundWorkStatus> {
        return repository.getForegroundWorkStatus()
    }
}