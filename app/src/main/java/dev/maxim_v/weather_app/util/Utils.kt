package dev.maxim_v.weather_app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun mapTimeStampToDate(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("d, MMMM yyyy, HH:mm", Locale.getDefault()).format(date)
}