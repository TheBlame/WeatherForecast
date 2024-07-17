package dev.maxim_v.weather_app.data

import androidx.datastore.core.DataStore
import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.database.dbmodels.toCurrent
import dev.maxim_v.weather_app.data.database.dbmodels.toDaily
import dev.maxim_v.weather_app.data.database.dbmodels.toHourly
import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.data.geocoder.GeocoderSource
import dev.maxim_v.weather_app.data.network.api.ForecastRequest
import dev.maxim_v.weather_app.data.network.api.RequestResult
import dev.maxim_v.weather_app.data.network.dto.toCurrentForecastDbModel
import dev.maxim_v.weather_app.data.network.dto.toDailyForecastDbModelList
import dev.maxim_v.weather_app.data.network.dto.toHourlyForecastDbModelList
import dev.maxim_v.weather_app.data.network.queryparams.Current
import dev.maxim_v.weather_app.data.network.queryparams.Daily
import dev.maxim_v.weather_app.data.network.queryparams.Hourly
import dev.maxim_v.weather_app.data.network.source.ForecastSource
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
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
    private val ds: DataStore<UserPref>
) : WeatherRepository {

    override suspend fun getWeather(vararg weatherSample: WeatherSample): Flow<Map<WeatherSample, WeatherModel>> =
        if (weatherSample.contains(HOURLY)) {
            fullFlow(getPref())
        } else {
            onlyCurrentFlow(getPref())
        }

    private fun onlyCurrentFlow(pref: UserPref) = flow {
        val request = ForecastRequest(
            latitude = pref.location.latitude,
            longitude = pref.location.longitude,
            currentArgs = listOf(
                Current.TEMPERATURE,
                Current.CODE,
                Current.APPARENT,
                Current.HUMIDITY,
                Current.WIND_SPEED,
                Current.WIND_DIRECTION
            ),
            hourlyArgs = null,
            dailyArgs = null,
            temperatureUnit = pref.tempUnit,
            windSpeedUnit = pref.windSpeedUnit,
            days = 1
        )
        val result = forecastSource.getForecast(request)

        if (result is RequestResult.Success) {
            result.content.toCurrentForecastDbModel()
                ?.let { db.forecastDao().insertCurrentForecast(it) }
        }

        val current = db.forecastDao().getCurrentForecast()
        val location = geocoderSource.getLocationName(current.latitude, current.longitude)

        emit(mapOf(CURRENT to current.toCurrent(location)))
    }

    private fun fullFlow(pref: UserPref) = flow {
        val request = ForecastRequest(
            latitude = pref.location.latitude,
            longitude = pref.location.longitude,
            currentArgs = listOf(
                Current.TEMPERATURE,
                Current.CODE,
                Current.APPARENT,
                Current.HUMIDITY,
                Current.WIND_SPEED,
                Current.WIND_DIRECTION
            ),
            hourlyArgs = listOf(Hourly.TEMPERATURE, Hourly.CODE),
            dailyArgs = listOf(Daily.MAX_TEMPERATURE, Daily.MIN_TEMPERATURE, Daily.CODE),
            temperatureUnit = pref.tempUnit,
            windSpeedUnit = pref.windSpeedUnit,
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
        val location = geocoderSource.getLocationName(current.latitude, current.longitude)

        emit(
            mapOf(
                CURRENT to current.toCurrent(pref.location.city),
                HOURLY to WeatherModel.HourlySample(hourly.map { it.toHourly() }),
                DAILY to WeatherModel.DailySample(daily.map { it.toDaily() })
            )
        )
    }

    private suspend fun getPref(): UserPref {
        return ds.data.first()
    }
}