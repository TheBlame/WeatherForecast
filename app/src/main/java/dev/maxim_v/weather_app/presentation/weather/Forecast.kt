package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.domain.entity.WeatherModel.CurrentSample
import dev.maxim_v.weather_app.domain.entity.WeatherModel.DailySample
import dev.maxim_v.weather_app.domain.entity.WeatherModel.HourlySample
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme

@Composable
fun Forecast(modifier: Modifier = Modifier, currentSample: CurrentSample, hourlySample: HourlySample, dailySample: DailySample) {
    Column(
        modifier = modifier
    ) {
        CurrentWeatherCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),


            currentWeather = currentSample
        )
        HourlyChart(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1800.dp),
            data = hourlySample.data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
            secondaryGraphLineAlpha = 0.5f,
            graphGradientColor = MaterialTheme.colorScheme.inversePrimary,
            graphGradientAlpha = 0.5f,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            timeTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        DailyChart(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1000.dp),
            data = dailySample.data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            mainGraphLineWidth = 16.dp,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            dateTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}