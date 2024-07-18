package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.domain.entity.WeatherType
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme

@Composable
fun HourlyChart(
    modifier: Modifier = Modifier,
    data: List<HourlyForecast>,
    mainGraphLineColor: Color,
    secondaryGraphLineColor: Color,
    secondaryGraphLineAlpha: Float,
    graphGradientColor: Color,
    graphGradientAlpha: Float,
    valueTextStyle: TextStyle = LocalTextStyle.current,
    timeTextStyle: TextStyle = LocalTextStyle.current
) {
    val transparentGraphColor = graphGradientColor.copy(alpha = graphGradientAlpha)
    val textMeasurer = rememberTextMeasurer()
    val iconSize = 64
    val context = LocalContext.current

    Canvas(modifier = modifier) {
        val valueTextPadding = 4.dp.toPx()
        val timeTextPadding = 16.dp.toPx()
        val graphBottomLinePadding = 64.dp.toPx()
        val graphValueTopPadding = 32.dp.toPx()
        val graphValueBottomPadding = 128.dp.toPx()
        val contentHorizontalPadding = 32.dp.toPx()
        val iconPadding = 44.dp.toPx()
        val xStep = size.width / (data.size - 1) - contentHorizontalPadding * 2 / (data.size - 1)
        val chartCoordinates = HourlyChartCoordinateProvider(
            data,
            graphValueTopPadding,
            size.height - graphValueBottomPadding,
            contentHorizontalPadding,
            xStep
        )

        drawLine(
            start = Offset(contentHorizontalPadding, size.height - graphBottomLinePadding),
            end = Offset(
                size.width - contentHorizontalPadding,
                size.height - graphBottomLinePadding
            ),
            color = secondaryGraphLineColor.copy(alpha = secondaryGraphLineAlpha),
            strokeWidth = 1f
        )

        chartCoordinates.forEachIndexed { index, chartCoordinate ->
            val hour = chartCoordinate.xValue
            val measuredText = textMeasurer.measure(hour, timeTextStyle)

            drawText(
                textMeasurer = textMeasurer,
                text = hour,
                style = timeTextStyle,
                topLeft = Offset(
                    contentHorizontalPadding + (index * xStep - measuredText.size.width / 2f),
                    size.height - measuredText.size.height - timeTextPadding
                )
            )
        }

        val graphPath = Path().apply {
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

                if (index == 0) {
                    moveTo(coordinate.x, coordinate.y)
                }

                lineTo(coordinate.x, coordinate.y)

                drawLine(
                    start = Offset(coordinate.x, coordinate.y),
                    end = Offset(coordinate.x, size.height - graphBottomLinePadding),
                    color = secondaryGraphLineColor.copy(alpha = secondaryGraphLineAlpha),
                    strokeWidth = 1f
                )

                drawCircle(
                    color = mainGraphLineColor,
                    radius = 2.dp.toPx(),
                    center = Offset(coordinate.x, coordinate.y)
                )

                imageToDraw.prepareToDraw()
                drawImage(
                    imageToDraw,
                    Offset(coordinate.x - iconSize / 2, size.height - iconSize / 2 - iconPadding)
                )

                val measuredText = textMeasurer.measure(coordinate.yValue, valueTextStyle)

                drawText(
                    textMeasurer = textMeasurer,
                    text = coordinate.yValue,
                    style = valueTextStyle,
                    topLeft = Offset(
                        coordinate.x - measuredText.size.width / 2f,
                        coordinate.y - measuredText.size.height - valueTextPadding
                    )
                )
            }
        }

        drawPath(
            path = graphPath,
            color = mainGraphLineColor,
            style = Stroke(
                width = 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        val fillPath =
            android.graphics.Path(graphPath.asAndroidPath()).asComposePath().apply {
                lineTo(size.width - contentHorizontalPadding, size.height - graphBottomLinePadding)
                lineTo(contentHorizontalPadding, size.height - graphBottomLinePadding)
                close()
            }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height
            )
        )
    }
}

@Preview
@Composable
private fun PreviewChart() {
    val data = listOf(
        HourlyForecast("00:00", -10, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("01:00", 0, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("02:00", 10, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("03:00", 23, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("04:00", 25, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("05:00", 30, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("06:00", 23, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("07:00", 23, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("08:00", 24, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("09:00", 24, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("10:00", 24, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("11:00", 25, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("12:00", 23, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("13:00", 24, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("14:00", 22, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("15:00", 25, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("16:00", 23, WeatherType.entries.toTypedArray().random()),
        HourlyForecast("18:00", 25, WeatherType.entries.toTypedArray().random()),
    )

    WeatherForecastTheme(darkTheme = true) {
        HourlyChart(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .horizontalScroll(rememberScrollState())
                .height(300.dp)
                .width(800.dp),
            data = data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
            secondaryGraphLineAlpha = 0.5f,
            graphGradientColor = MaterialTheme.colorScheme.inversePrimary,
            graphGradientAlpha = 0.5f,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onBackground),
            timeTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}