package dev.maxim_v.weather_app.data.network.dto

import dev.maxim_v.weather_app.data.database.dbmodels.CurrentForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.DailyForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.HourlyForecastDbModel

fun ForecastDto.toCurrentForecastDbModel(): CurrentForecastDbModel? {
    return if (this.current != null) {
        CurrentForecastDbModel(
            temp = this.current.temperature2m,
            apparentTemp = this.current.apparentTemperature,
            precipitation = this.current.precipitation,
            humidity = this.current.relativeHumidity2m,
            windSpeed = this.current.windSpeed10m,
            windDirection = this.current.windDirection10m,
            weatherCode = this.current.weatherCode,
            latitude = this.latitude,
            longitude = this.longitude,
            timestamp = this.current.time
        )
    } else null
}

fun ForecastDto.toHourlyForecastDbModelList(): List<HourlyForecastDbModel>? {
    return if (this.hourly != null) {
        val temp = this.hourly.temperature2m
        val weatherCode = this.hourly.weatherCode
        val timeStamp = this.hourly.time
        buildList {
            for (time in timeStamp.withIndex()) {
                add(
                    HourlyForecastDbModel(
                        temp = temp[time.index],
                        weatherCode = weatherCode[time.index],
                        timestamp = time.value
                    )
                )
            }
        }
    } else null
}

fun ForecastDto.toDailyForecastDbModelList(): List<DailyForecastDbModel>? {
    return if (this.daily != null) {
        val minTemp = this.daily.temperature2mMin
        val maxTemp = this.daily.temperature2mMax
        val weatherCode = this.daily.weatherCode
        val timeStamp = this.daily.time
        buildList {
            for (time in timeStamp.withIndex()) {
                add(
                    DailyForecastDbModel(
                        minTemp = minTemp[time.index],
                        maxTemp = maxTemp[time.index],
                        weatherCode = weatherCode[time.index],
                        timestamp = time.value
                    )
                )
            }
        }
    } else null
}