package dev.maxim_v.weather_app.data.database.dbmodels

import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.util.mapTimeStampToDate

fun CurrentForecastDbModel.toCurrent(): WeatherModel.CurrentSample {
    return WeatherModel.CurrentSample(
        location = this.location,
        time = mapTimeStampToDate(this.timestamp * 1000),
        temp = this.temp.toString(),
        weatherType = this.weatherCode.toWeatherType()
    )
}