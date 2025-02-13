package dev.maxim_v.weather_app.data

import androidx.datastore.core.DataStore
import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.database.dbmodels.toCurrent
import dev.maxim_v.weather_app.data.database.dbmodels.toDaily
import dev.maxim_v.weather_app.data.database.dbmodels.toHourly
import dev.maxim_v.weather_app.data.datastore.ForecastWorkerData
import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.data.datastore.UserSavedLocation
import dev.maxim_v.weather_app.data.datastore.toUserSettings
import dev.maxim_v.weather_app.data.geocoder.AppGeocoder
import dev.maxim_v.weather_app.data.location.LocationService
import dev.maxim_v.weather_app.data.network.api.RequestResult
import dev.maxim_v.weather_app.data.network.api.forecastApi.ForecastRequest
import dev.maxim_v.weather_app.data.network.dto.toCurrentForecastDbModel
import dev.maxim_v.weather_app.data.network.dto.toDailyForecastDbModelList
import dev.maxim_v.weather_app.data.network.dto.toForecastWorkerData
import dev.maxim_v.weather_app.data.network.dto.toHourlyForecastDbModelList
import dev.maxim_v.weather_app.data.network.queryparams.CurrentParams
import dev.maxim_v.weather_app.data.network.queryparams.DailyParams
import dev.maxim_v.weather_app.data.network.queryparams.HourlyParams
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnitParams
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnitParams
import dev.maxim_v.weather_app.data.network.source.ForecastSource
import dev.maxim_v.weather_app.domain.entity.ForegroundWorkStatus
import dev.maxim_v.weather_app.domain.entity.FullForecast
import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.exceptions.DatabaseException
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val forecastSource: ForecastSource,
    private val appGeocoder: AppGeocoder,
    private val db: AppDatabase,
    private val locationDs: DataStore<UserSavedLocation>,
    private val prefDs: DataStore<UserPref>,
    private val forecastWorkerDs: DataStore<ForecastWorkerData>,
    private val locationService: LocationService
) : WeatherRepository {

    override suspend fun getFullForecast(): Flow<FullForecast> = flow {
        val pref = getPref()
        val location = getSavedLocation()
        val request = ForecastRequest(
            latitude = location.latitude,
            longitude = location.longitude,
            currentParams = listOf(
                CurrentParams.TEMPERATURE,
                CurrentParams.CODE,
                CurrentParams.APPARENT,
                CurrentParams.HUMIDITY,
                CurrentParams.WIND_SPEED,
                CurrentParams.WIND_DIRECTION
            ),
            hourlyParams = listOf(HourlyParams.TEMPERATURE, HourlyParams.CODE),
            dailyParams = listOf(
                DailyParams.MAX_TEMPERATURE,
                DailyParams.MIN_TEMPERATURE,
                DailyParams.CODE
            ),
            temperatureUnitParam = TemperatureUnitParams.getFromPref(pref),
            windSpeedUnitParam = WindSpeedUnitParams.getFromPref(pref),
            days = 15
        )
        val result = forecastSource.getForecast(request)

        if (result is RequestResult.Success) {
            withContext(Dispatchers.IO) {
                db.clearAllTables()
            }
            result.content.toCurrentForecastDbModel()
                ?.let { db.forecastDao().insertCurrentForecast(it) }
            result.content.toHourlyForecastDbModelList()
                ?.let { db.forecastDao().insertHourlyForecast(it) }
            result.content.toDailyForecastDbModelList()
                ?.let { db.forecastDao().insertDailyForecast(it) }
        }

        val current = db.forecastDao().getCurrentForecast()
        val hourly = db.forecastDao().getHourlyForecast(System.currentTimeMillis() / 1000)
        val daily = db.forecastDao().getDailyForecast()

        if (current != null) {
            emit(
                FullForecast(
                    location = location.city,
                    currentForecast = current.toCurrent(),
                    hourlyForecast = hourly.map { it.toHourly() },
                    dailyForecast = daily.map { it.toDaily() }
                )
            )
            if (result is RequestResult.Error) throw NetworkException()
        } else throw DatabaseException()
    }

    override suspend fun getLocationWithGps() {
        val newLocation = locationService.getLocation().first()
        locationDs.updateData {
            val newCity =
                appGeocoder.getLocationName(newLocation.latitude, newLocation.longitude)
            it.copy(
                latitude = newLocation.latitude,
                longitude = newLocation.longitude,
                city = newCity
            )
        }
    }

    override fun getCurrentSettings(): Flow<UserSettings> = prefDs.data.map { it.toUserSettings() }

    override suspend fun updateSettings(newSettings: UserSettings) {
        prefDs.updateData {
            it.copy(
                tempUnit = newSettings.tempUnit,
                windSpeedUnit = newSettings.windSpeedUnit,
                theme = newSettings.theme,
                notification = newSettings.notification
            )
        }
    }

    override fun getAppTheme(): Flow<ThemeType> = prefDs.data.map { it.theme }

    override suspend fun getLocationWithGeocoding(city: String): Flow<List<SearchedLocation>> =
        flow {
            emit(
                appGeocoder.getLocationFromName(city)
            )
        }

    override suspend fun saveLocation(location: SearchedLocation) {
        locationDs.updateData {
            it.copy(
                latitude = location.latitude,
                longitude = location.longitude,
                city = location.city
            )
        }
    }

    override suspend fun loadCurrentTempAndWeather(highPriority: Boolean) {
        val timestamp = System.currentTimeMillis()
        val pref = getPref()
        val workerData = getWorkerData()
        val location = getSavedLocation()
        if (highPriority || workerData.timestamp < timestamp - DATA_FRESHNESS_OFFSET) {
            val request = ForecastRequest(
                latitude = location.latitude,
                longitude = location.longitude,
                currentParams = listOf(
                    CurrentParams.TEMPERATURE, CurrentParams.CODE
                ),
                hourlyParams = null,
                dailyParams = null,
                temperatureUnitParam = TemperatureUnitParams.getFromPref(pref),
                windSpeedUnitParam = null,
                days = null
            )
            val result = forecastSource.getForecast(request)

            if (result is RequestResult.Success) {
                val newDsData = result.content.toForecastWorkerData(timestamp)
                if (newDsData != null) {
                    forecastWorkerDs.updateData {
                        it.copy(
                            temp = newDsData.temp,
                            weatherType = newDsData.weatherType,
                            timestamp = timestamp
                        )
                    }
                }
            } else throw NetworkException()
        }
    }

    override fun getForegroundWorkStatus() =
        combine(prefDs.data, locationDs.data) { pref, location ->
            ForegroundWorkStatus(
                enabled = pref.notification,
                location = location.city,
                temperatureUnit = pref.tempUnit
            )
        }

    private suspend fun getPref(): UserPref {
        return prefDs.data.first()
    }

    private suspend fun getSavedLocation(): UserSavedLocation {
        return locationDs.data.first()
    }

    private suspend fun getWorkerData(): ForecastWorkerData {
        return forecastWorkerDs.data.first()
    }

    companion object {
        const val DATA_FRESHNESS_OFFSET = 900000L
    }
}