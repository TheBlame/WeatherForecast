package dev.maxim_v.weather_app.data.datastore

import dev.maxim_v.weather_app.domain.entity.CurrentTempAndWeather
import dev.maxim_v.weather_app.domain.entity.UserSettings

fun UserPref.toUserSettings(): UserSettings {
    return UserSettings(
        tempUnit = this.tempUnit,
        windSpeedUnit = this.windSpeedUnit,
        theme = this.theme,
        notification = this.notification
    )
}

fun ForecastWorkerData.toCurrentTempAndWeather(): CurrentTempAndWeather {
    return CurrentTempAndWeather(
        temp = this.temp,
        weatherType = this.weatherType
    )
}