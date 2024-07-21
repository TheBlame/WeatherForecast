package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.FullForecast

@Immutable
sealed class MainScreenState {
    data object Initial : MainScreenState()
    data object Loading : MainScreenState()
    data class Content(val data: FullForecast) : MainScreenState()
    data object NoContent : MainScreenState()
}