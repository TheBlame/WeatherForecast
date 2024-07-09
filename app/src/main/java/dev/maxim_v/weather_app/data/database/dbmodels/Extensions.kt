package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.util.Direction
import dev.maxim_v.weather_app.util.mapTimeStampToDate
import kotlin.math.roundToInt

private const val MILLIS_IN_SECONDS = 1000

fun CurrentForecastDbModel?.toCurrent(location: String): WeatherModel.CurrentSample {
    return if (this != null) {
        WeatherModel.CurrentSample(
            location = location,
            time = mapTimeStampToDate(this.timestamp * MILLIS_IN_SECONDS),
            temp = "${this.temp.roundToInt()}",
            apparentTemp = "${this.apparentTemp.roundToInt()}",
            humidity = "${this.humidity}",
            windSpeed = "${this.windSpeed.roundToInt()}",
            windDirection = Direction[this.windDirection].name,
            precipitation = "${this.precipitation}",
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        WeatherModel.CurrentSample()
    }
}