package dev.maxim_v.weather_app.data

import androidx.datastore.core.DataStore
import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.database.dbmodels.toCurrent
import dev.maxim_v.weather_app.data.database.dbmodels.toDaily
import dev.maxim_v.weather_app.data.database.dbmodels.toHourly
import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.data.datastore.UserSavedLocation
import dev.maxim_v.weather_app.data.geocoder.GeocoderSource
import dev.maxim_v.weather_app.data.location.LocationService
import dev.maxim_v.weather_app.data.network.api.ForecastRequest
import dev.maxim_v.weather_app.data.network.api.RequestResult
import dev.maxim_v.weather_app.data.network.dto.toCurrentForecastDbModel
import dev.maxim_v.weather_app.data.network.dto.toDailyForecastDbModelList
import dev.maxim_v.weather_app.data.network.dto.toHourlyForecastDbModelList
import dev.maxim_v.weather_app.data.network.queryparams.CurrentParams
import dev.maxim_v.weather_app.data.network.queryparams.DailyParams
import dev.maxim_v.weather_app.data.network.queryparams.HourlyParams
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnitParams
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnitParams
import dev.maxim_v.weather_app.data.network.source.ForecastSource
import dev.maxim_v.weather_app.domain.entity.FullForecast
import dev.maxim_v.weather_app.domain.exceptions.DatabaseException
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val forecastSource: ForecastSource,
    private val geocoderSource: GeocoderSource,
    private val db: AppDatabase,
    private val locationDs: DataStore<UserSavedLocation>,
    private val prefDs: DataStore<UserPref>,
    private val locationService: LocationService
) : WeatherRepository {

    override suspend fun getFullForecast(): Flow<FullForecast> = flow {
        val pref = getPref()
        val savedLocation = getSavedLocation()

        val request = ForecastRequest(
            latitude = savedLocation.latitude,
            longitude = savedLocation.longitude,
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
                    location = savedLocation.city,
                    currentForecast = current.toCurrent(),
                    hourlyForecast = hourly.map { it.toHourly() },
                    dailyForecast = daily.map { it.toDaily() }
                )
            )
            if (result is RequestResult.Error) throw NetworkException()
        } else {
            throw DatabaseException()
        }
    }

    override suspend fun getLocationWithGps() {
        val newLocation = locationService.getLocation().first()
        locationDs.updateData {
            val newCity =
                geocoderSource.getLocationName(newLocation.latitude, newLocation.longitude)
            it.copy(
                latitude = newLocation.latitude,
                longitude = newLocation.longitude,
                city = newCity
            )
        }
    }

//    private fun onlyCurrentFlow(pref: UserSavedLocation) = flow {
//        val request = ForecastRequest(
//            latitude = pref.latitude,
//            longitude = pref.longitude,
//            currentParams = listOf(
//                CurrentParams.TEMPERATURE,
//                CurrentParams.CODE,
//                CurrentParams.APPARENT,
//                CurrentParams.HUMIDITY,
//                CurrentParams.WIND_SPEED,
//                CurrentParams.WIND_DIRECTION
//            ),
//            hourlyParams = null,
//            dailyParams = null,
//            temperatureUnitParam = TemperatureUnitParams.getFromPref(pref),
//            windSpeedUnitParam = WindSpeedUnitParams.getFromPref(pref),
//            days = 1
//        )
//        val result = forecastSource.getForecast(request)
//
//        if (result is RequestResult.Success) {
//            result.content.toCurrentForecastDbModel()
//                ?.let { db.forecastDao().insertCurrentForecast(it) }
//        }
//
//        val current = db.forecastDao().getCurrentForecast()
//        val location = geocoderSource.getLocationName(current!!.latitude, current.longitude)
//
//        emit(mapOf(CURRENT to current!!.toCurrent()))
//    }


    private suspend fun getPref(): UserPref {
        return prefDs.data.first()
    }

    private suspend fun getSavedLocation(): UserSavedLocation {
        return locationDs.data.first()
    }
}