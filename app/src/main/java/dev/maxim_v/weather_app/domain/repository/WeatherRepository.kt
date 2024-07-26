package dev.maxim_v.weather_app.domain.repository

import dev.maxim_v.weather_app.domain.entity.ForegroundWorkStatus
import dev.maxim_v.weather_app.domain.entity.FullForecast
import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getFullForecast(): Flow<FullForecast>

    suspend fun getLocationWithGps()

    fun getCurrentSettings(): Flow<UserSettings>

    suspend fun updateSettings(newSettings: UserSettings)

    fun getAppTheme(): Flow<ThemeType>

    suspend fun getLocationWithGeocoding(city: String): Flow<List<SearchedLocation>>

    suspend fun saveLocation(location: SearchedLocation)

    suspend fun getCurrentTemp(): String

    fun getForegroundWorkStatus(): Flow<ForegroundWorkStatus>
}