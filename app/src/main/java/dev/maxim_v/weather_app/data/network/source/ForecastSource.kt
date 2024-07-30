package dev.maxim_v.weather_app.data.network.source

import dev.maxim_v.weather_app.data.network.api.forecastApi.ForecastApi
import dev.maxim_v.weather_app.data.network.api.forecastApi.ForecastRequest
import javax.inject.Inject

class ForecastSource @Inject constructor(private val forecastApi: ForecastApi): BaseNetworkSource() {

    suspend fun getForecast(request: ForecastRequest) =
        wrapRetrofitExceptions {
            forecastApi.loadForecast(
                latitude = request.latitude,
                longitude = request.longitude,
                currentParams = request.currentParams,
                hourlyParams = request.hourlyParams,
                dailyParams = request.dailyParams,
                tempUnit = request.temperatureUnitParam,
                windSpeedUnitParam = request.windSpeedUnitParam,
                forecastDays = request.days
            )
        }
}