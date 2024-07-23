package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.usecases.GetAppThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppThemeViewModel @Inject constructor(
    private val getAppThemeUseCase: GetAppThemeUseCase
) : ViewModel() {

    val appTheme = getAppThemeUseCase().map { AppThemeState.Ready(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AppThemeState.Initial)
}