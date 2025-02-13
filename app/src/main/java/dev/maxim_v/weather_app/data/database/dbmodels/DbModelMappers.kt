package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.CurrentForecast
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.util.Direction
import dev.maxim_v.weather_app.util.mapTimeStampToDay
import dev.maxim_v.weather_app.util.mapTimeStampToHours
import kotlin.math.roundToInt

private const val MILLIS_IN_SECONDS = 1000

fun CurrentForecastDbModel.toCurrent(): CurrentForecast {
    return CurrentForecast(
        temp = this.temp.roundToInt().toString(),
        apparentTemp = this.apparentTemp.roundToInt().toString(),
        humidity = this.humidity.toString(),
        windSpeed = this.windSpeed.roundToInt().toString(),
        windDirection = Direction[this.windDirection],
        precipitation = this.precipitation.toString(),
        temperatureUnit = this.temperatureUnit,
        windSpeedUnit = this.windSpeedUnit,
        weatherType = this.weatherType
    )
}

fun HourlyForecastDbModel.toHourly(): HourlyForecast {
    return HourlyForecast(
        time = mapTimeStampToHours(this.timestamp * MILLIS_IN_SECONDS),
        temp = this.temp.roundToInt(),
        weatherType = this.weatherType
    )
}

fun DailyForecastDbModel.toDaily(): DailyForecast {
    return DailyForecast(
        date = mapTimeStampToDay(this.timestamp * MILLIS_IN_SECONDS),
        minTemp = this.minTemp.roundToInt(),
        maxTemp = this.maxTemp.roundToInt(),
        weatherType = this.weatherType
    )
}