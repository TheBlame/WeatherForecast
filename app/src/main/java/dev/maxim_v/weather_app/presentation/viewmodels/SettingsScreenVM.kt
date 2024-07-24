package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.usecases.GetCurrentSettingsUseCase
import dev.maxim_v.weather_app.domain.usecases.UpdateSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenVM @Inject constructor(
    private val getCurrentSettingsUseCase: GetCurrentSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {

    val settingsScreenState = getCurrentSettingsUseCase()
        .map { SettingsScreenState.Ready(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SettingsScreenState.Initial)

    fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.SettingUpdate -> updateSetting(event.newSettings)
        }
    }

    private fun updateSetting(newSettings: UserSettings) {
        viewModelScope.launch {
            updateSettingsUseCase.invoke(newSettings)
        }
    }
}