package dev.maxim_v.weather_app.util

import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.WeatherType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun mapTimeStampToDate(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("d, MMMM yyyy, HH:mm", Locale.getDefault()).format(date)
}

fun mapTimeStampToDay(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("d", Locale.getDefault()).format(date)
}

fun mapTimeStampToHours(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("d, HH:mm", Locale.getDefault()).format(date)
}

fun selectIcon(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.CLEAR -> R.drawable.sunny
        WeatherType.SNOW -> R.drawable.snowy
        WeatherType.RAIN -> R.drawable.rainy
        WeatherType.THUNDER -> R.drawable.rainthunder
        WeatherType.CLOUDY -> R.drawable.partlycloudy
    }
}