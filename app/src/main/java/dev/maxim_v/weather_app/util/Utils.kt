package dev.maxim_v.weather_app.util

import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit.FAHRENHEIT
import dev.maxim_v.weather_app.domain.entity.ThemeType
import dev.maxim_v.weather_app.domain.entity.ThemeType.AUTO
import dev.maxim_v.weather_app.domain.entity.ThemeType.DARK
import dev.maxim_v.weather_app.domain.entity.ThemeType.LIGHT
import dev.maxim_v.weather_app.domain.entity.WeatherType
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit.KMH
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit.MS
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

@Composable
fun ThemeType.stringResource(): String {
    return when (this) {
        AUTO -> stringResource(id = R.string.auto)
        LIGHT -> stringResource(id = R.string.light)
        DARK -> stringResource(id = R.string.dark)
    }
}

@Composable
fun TemperatureUnit.stringResource(): String {
    return when (this) {
        CELSIUS -> stringResource(id = R.string.celsius)
        FAHRENHEIT -> stringResource(id = R.string.fahrenheit)
    }
}

fun TemperatureUnit.getString(context: Context): String {
    return when (this) {
        CELSIUS -> context.getString(R.string.celsius)
        FAHRENHEIT -> context.getString(R.string.fahrenheit)
    }
}

@Composable
fun WindSpeedUnit.stringResource(context: Context? = null): String {
    return when (this) {
        MS -> stringResource(id = R.string.ms)
        KMH -> stringResource(id = R.string.kmh)
    }
}

fun WindSpeedUnit.getString(context: Context): String {
    return when (this) {
        MS -> context.getString(R.string.ms)
        KMH -> context.getString(R.string.kmh)
    }
}