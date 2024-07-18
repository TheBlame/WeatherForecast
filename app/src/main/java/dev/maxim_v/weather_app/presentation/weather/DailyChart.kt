package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.WeatherType
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.measureAndDrawText

@Composable
fun DailyChart(
    modifier: Modifier = Modifier,
    data: List<DailyForecast>,
    mainGraphLineColor: Color,
    mainGraphLineWidth: Dp,
    valueTextStyle: TextStyle = LocalTextStyle.current,
    dateTextStyle: TextStyle = LocalTextStyle.current
) {
    val textMeasurer = rememberTextMeasurer()
    val iconSize = 64
    val context = LocalContext.current

    Canvas(modifier = modifier) {
        val valueTextPadding = 12.dp.toPx()
        val timeTextPadding = 16.dp.toPx()
        val graphBottomLinePadding = 72.dp.toPx()
        val graphValueTopPadding = 32.dp.toPx()
        val graphValueBottomPadding = 96.dp.toPx()
        val contentHorizontalPadding = 32.dp.toPx()
        val iconPadding = 38.dp.toPx()
        val xStep = size.width / (data.size - 1) - contentHorizontalPadding * 2 / (data.size - 1)
        val chartCoordinates = DailyChartCoordinateProvider(
            data,
            graphValueTopPadding,
            size.height - graphValueBottomPadding,
            contentHorizontalPadding,
            xStep
        )

        chartCoordinates.forEachIndexed { index, coordinate ->
            val imageToDraw = when (coordinate.weatherType) {
                WeatherType.CLEAR -> context.getDrawable(R.drawable.sunny)?.toBitmap(iconSize, iconSize)
                    ?.asImageBitmap()
                    ?: ImageBitmap(0, 0)
                WeatherType.SNOW -> context.getDrawable(R.drawable.partlycloudy)?.toBitmap(iconSize, iconSize)
                    ?.asImageBitmap()
                    ?: ImageBitmap(0, 0)
                WeatherType.RAIN -> context.getDrawable(R.drawable.rainy)?.toBitmap(iconSize, iconSize)
                    ?.asImageBitmap()
                    ?: ImageBitmap(0, 0)
                WeatherType.THUNDER -> context.getDrawable(R.drawable.rainthunder)?.toBitmap(iconSize, iconSize)
                    ?.asImageBitmap()
                    ?: ImageBitmap(0, 0)
                WeatherType.CLOUDY -> context.getDrawable(R.drawable.snowy)?.toBitmap(iconSize, iconSize)
                    ?.asImageBitmap()
                    ?: ImageBitmap(0, 0)
            }

            val xValueText = when(index) {
                0 -> context.getString(R.string.today)
                1 -> context.getString(R.string.tomorrow)
                else -> coordinate.xValue
            }

            measureAndDrawText(
                textMeasurer,
                coordinate.maxXValue,
                Offset(coordinate.x, coordinate.y - valueTextPadding),
                valueTextStyle
            )

            measureAndDrawText(
                textMeasurer,
                coordinate.minXValue,
                Offset(coordinate.x, size.height - graphBottomLinePadding + valueTextPadding),
                valueTextStyle
            )

            measureAndDrawText(
                textMeasurer,
                xValueText,
                Offset(contentHorizontalPadding + index * xStep,size.height - timeTextPadding),
                dateTextStyle
            )

            drawLine(
                start = Offset(coordinate.x, size.height - graphBottomLinePadding - mainGraphLineWidth.toPx() / 2),
                end = Offset(coordinate.x, coordinate.y + mainGraphLineWidth.toPx() / 2),
                color = mainGraphLineColor,
                strokeWidth = mainGraphLineWidth.toPx(),
                cap = StrokeCap.Round
            )

            imageToDraw.prepareToDraw()
            drawImage(
                imageToDraw,
                Offset(coordinate.x - iconSize / 2, size.height - iconSize / 2 - iconPadding)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChart() {
    val data = listOf(
        DailyForecast("16.07", 14, 24, WeatherType.entries.toTypedArray().random()),
        DailyForecast("17.07", 16, 27, WeatherType.entries.toTypedArray().random()),
        DailyForecast("18.07", 15, 25, WeatherType.entries.toTypedArray().random()),
        DailyForecast("19.07", 14, 0, WeatherType.entries.toTypedArray().random()),
        DailyForecast("20.07", 15, 27, WeatherType.entries.toTypedArray().random()),
    )

    WeatherForecastTheme(darkTheme = true) {
        DailyChart(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp))
                .width(800.dp)
                .height(200.dp),
            data = data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            mainGraphLineWidth = 16.dp,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onBackground),
            dateTextStyle = ReplacementTheme.typography.extraSmall.copy(MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}