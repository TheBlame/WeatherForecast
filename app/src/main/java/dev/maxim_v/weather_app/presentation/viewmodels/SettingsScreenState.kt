package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.compose.runtime.Immutable
import dev.maxim_v.weather_app.domain.entity.UserSettings

@Immutable
sealed class SettingsScreenState {
    data object Initial : SettingsScreenState()
    data class Ready(val setting: UserSettings) : SettingsScreenState()
}