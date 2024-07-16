package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.util.Direction
import dev.maxim_v.weather_app.util.mapTimeStampToDate
import dev.maxim_v.weather_app.util.mapTimeStampToDay
import dev.maxim_v.weather_app.util.mapTimeStampToHours
import kotlin.math.roundToInt

private const val MILLIS_IN_SECONDS = 1000

fun CurrentForecastDbModel?.toCurrent(location: String): WeatherModel.CurrentSample {
    return if (this != null) {
        WeatherModel.CurrentSample(
            location = location,
            time = mapTimeStampToDate(this.timestamp * MILLIS_IN_SECONDS),
            temp = this.temp.roundToInt().toString(),
            apparentTemp = this.apparentTemp.roundToInt().toString(),
            humidity = this.humidity.toString(),
            windSpeed = this.windSpeed.roundToInt().toString(),
            windDirection = Direction[this.windDirection].name,
            precipitation = this.precipitation.toString(),
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        WeatherModel.CurrentSample()
    }
}

fun HourlyForecastDbModel?.toHourly(): HourlyForecast {
    return if (this != null) {
        HourlyForecast(
            time = mapTimeStampToHours(this.timestamp * MILLIS_IN_SECONDS),
            temp = this.temp.roundToInt(),
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        HourlyForecast()
    }
}

fun DailyForecastDbModel?.toDaily(): DailyForecast {
    return if (this != null) {
        DailyForecast(
            date = mapTimeStampToDay(this.timestamp * MILLIS_IN_SECONDS),
            minTemp = this.minTemp.roundToInt(),
            maxTemp = this.maxTemp.roundToInt(),
            weatherType = this.weatherCode.toWeatherType()
        )
    } else {
        DailyForecast()
    }
}