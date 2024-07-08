package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.util.mapTimeStampToDate

private const val MILLIS_IN_SECONDS = 1000

fun CurrentForecastDbModel?.toCurrent(): WeatherModel.CurrentSample {
    return if (this != null) {
        WeatherModel.CurrentSample(
            location = this.location,
            time = mapTimeStampToDate(this.timestamp * MILLIS_IN_SECONDS),
            temp = this.temp.toString(),
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        WeatherModel.CurrentSample()
    }
}