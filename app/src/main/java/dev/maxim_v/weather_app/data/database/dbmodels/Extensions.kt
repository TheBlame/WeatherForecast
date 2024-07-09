package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.util.Direction
import dev.maxim_v.weather_app.util.mapTimeStampToDate

private const val MILLIS_IN_SECONDS = 1000

fun CurrentForecastDbModel?.toCurrent(location: String): WeatherModel.CurrentSample {
    return if (this != null) {
        WeatherModel.CurrentSample(
            location = location,
            time = mapTimeStampToDate(this.timestamp * MILLIS_IN_SECONDS),
            temp = "${this.temp}${R.string.celsius}",
            apparentTemp = "${this.apparentTemp}${R.string.celsius}",
            humidity = "${this.humidity}${R.string.humidity}",
            windSpeed = "${this.windSpeed}${R.string.wind_speed}",
            windDirection = Direction[this.windDirection].name,
            precipitation = "${this.precipitation}${R.string.precipitation}",
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        WeatherModel.CurrentSample()
    }
}