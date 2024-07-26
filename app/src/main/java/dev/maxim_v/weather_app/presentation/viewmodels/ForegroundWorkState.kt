package dev.maxim_v.weather_app.presentation.viewmodels

import dev.maxim_v.weather_app.domain.entity.ForegroundWorkStatus

sealed class ForegroundWorkState {
    data object Initial : ForegroundWorkState()
    data class Data(val status: ForegroundWorkStatus) : ForegroundWorkState()
}