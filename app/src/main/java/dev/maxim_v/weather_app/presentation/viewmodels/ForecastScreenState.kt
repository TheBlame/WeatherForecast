package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.FullForecast

@Immutable
sealed class ForecastScreenState {
    data object Initial : ForecastScreenState()
    data object Loading : ForecastScreenState()
    data class Content(val data: FullForecast) : ForecastScreenState()
    data object NoContent : ForecastScreenState()
}