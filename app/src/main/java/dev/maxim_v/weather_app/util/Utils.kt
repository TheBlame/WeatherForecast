package dev.maxim_v.weather_app.util

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.core.graphics.drawable.IconCompat
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.FAHRENHEIT
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType.DARK
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType.LIGHT
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType.SYSTEM
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.KMH
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.MS
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

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
    textFormat: String,
    topLeft: Offset = Offset.Zero,
    style: TextStyle = TextStyle.Default
) {
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(text),
        style = style
    )

    drawText(
        textMeasurer = textMeasurer,
        text = String.format(textFormat, text),
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
        SYSTEM -> stringResource(id = R.string.system)
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
fun WindSpeedUnit.stringResource(): String {
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

@Composable
fun Direction.stringResource(): String {
    return when (this) {
        Direction.NORTH -> stringResource(id = R.string.north_direction)
        Direction.NORTH_EAST -> stringResource(id = R.string.north_east_direction)
        Direction.EAST -> stringResource(id = R.string.east_direction)
        Direction.SOUTH_EAST -> stringResource(id = R.string.south_east_direction)
        Direction.SOUTH -> stringResource(id = R.string.south_direction)
        Direction.SOUTH_WEST -> stringResource(id = R.string.south_west_direction)
        Direction.WEST -> stringResource(id = R.string.west_direction)
        Direction.NORTH_WEST -> stringResource(id = R.string.north_west_direction)
    }
}

@Composable
fun ThemeType.isDark(): Boolean {
    return when (this) {
        LIGHT -> false
        DARK -> true
        SYSTEM -> isSystemInDarkTheme()
    }
}

fun createIconFromString(inputNumber: String): IconCompat {
    val paint = Paint()
    paint.typeface = Typeface.DEFAULT_BOLD
    paint.textScaleX = if (inputNumber.length > 3) 0.55f else 0.75f
    paint.isAntiAlias = true
    paint.textSize = if (inputNumber.length > 2) 72f else 64f
    paint.textAlign = Paint.Align.CENTER

    val textBounds = Rect()
    paint.getTextBounds(inputNumber, 0, inputNumber.length, textBounds)
    val max = max(textBounds.width(), textBounds.height())

    val bitmap = Bitmap.createBitmap(
        max, max,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawText(
        inputNumber,
        canvas.width / 2f ,
        canvas.height / 2f + textBounds.height() / 2f,
        paint)
    return IconCompat.createWithBitmap(bitmap)
}