package dev.maxim_v.weather_app.presentation.viewmodels

import dev.maxim_v.weather_app.domain.entity.UserSettings

sealed class SettingsScreenEvent {
    data class SettingUpdate(val newSettings: UserSettings) : SettingsScreenEvent()
}