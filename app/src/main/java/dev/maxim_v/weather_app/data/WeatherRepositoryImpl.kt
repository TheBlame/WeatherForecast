package dev.maxim_v.weather_app.data

import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.database.dbmodels.toCurrent
import dev.maxim_v.weather_app.data.network.api.ForecastRequest
import dev.maxim_v.weather_app.data.network.api.RequestResult
import dev.maxim_v.weather_app.data.network.dto.toCurrentForecastDbModel
import dev.maxim_v.weather_app.data.network.queryparams.Current
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnit
import dev.maxim_v.weather_app.data.network.source.ForecastSource
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val forecastSource: ForecastSource,
    private val db: AppDatabase
) : WeatherRepository {

    override suspend fun getWeather(vararg weatherSample: WeatherSample): Flow<Map<WeatherSample, WeatherModel>> =
        flow {

            if (weatherSample.contains(CURRENT)) {
                val request = ForecastRequest(
                    latitude = 57.1522,
                    longitude = 65.5272,
                    currentArgs = listOf(Current.TEMPERATURE, Current.CODE),
                    hourlyArgs = null,
                    dailyArgs = null,
                    unit = TemperatureUnit.CELSIUS
                )
                val result = forecastSource.getForecast(request)

                if (result is RequestResult.Success) {
                    result.content.toCurrentForecastDbModel()
                        ?.let { db.forecastDao().insertCurrentForecast(it) }
                }

                emit(mapOf(CURRENT to db.forecastDao().getCurrentForecast().toCurrent("qwe")))
            }
        }
}