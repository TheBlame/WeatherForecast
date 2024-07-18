package dev.maxim_v.weather_app.util

import android.Manifest
import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.WeatherType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun mapTimeStampToDate(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault()).format(date)
}

fun mapTimeStampToDay(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("dd.MM", Locale.getDefault()).format(date)
}

fun mapTimeStampToHours(timeStamp: Long): String {
    val date = Date(timeStamp)
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
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

fun DrawScope.measureAndDrawText(
    textMeasurer: TextMeasurer,
    text: String,
    topLeft: Offset = Offset.Zero,
    style: TextStyle = TextStyle.Default
) {
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(text),
        style = style
    )

    drawText(
        textMeasurer = textMeasurer,
        text = text,
        style = style,
        topLeft = Offset(
            topLeft.x - textLayoutResult.size.width / 2f,
            topLeft.y - textLayoutResult.size.height / 2f
        )
    )
}

fun checkLocationPermissions(context: Context) =
    (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PERMISSION_GRANTED)