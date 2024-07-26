package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.usecases.GetAppThemeUseCase
import dev.maxim_v.weather_app.domain.usecases.GetForegroundWorkStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getForegroundWorkStatusUseCase: GetForegroundWorkStatusUseCase
) : ViewModel() {

    val appThemeState = getAppThemeUseCase().map { AppThemeState.Data(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AppThemeState.Initial)

    val foregroundWorkState = getForegroundWorkStatusUseCase().map { ForegroundWorkState.Data(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ForegroundWorkState.Initial)
}
