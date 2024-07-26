package dev.maxim_v.weather_app.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class ForegroundWorkStatus(
    val enabled: Boolean,
    val location: String
)
