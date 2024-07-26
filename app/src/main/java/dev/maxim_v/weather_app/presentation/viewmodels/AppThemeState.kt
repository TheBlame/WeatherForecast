package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType

@Immutable
sealed class AppThemeState {
    data object Initial : AppThemeState()
    data class Data(val theme: ThemeType) : AppThemeState()
}