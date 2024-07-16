package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.WeatherType
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.selectIcon

@Composable
fun DailyTempCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    dailyForecast: DailyForecast
) {
    Card(
        modifier = modifier,
        colors = colors,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dailyForecast.date,
                style = ReplacementTheme.typography.large
            )
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = selectIcon(dailyForecast.weatherType)),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.celsius, dailyForecast.maxTemp),
                        style = ReplacementTheme.typography.small
                    )
                    Text(
                        text = stringResource(id = R.string.max),
                        style = ReplacementTheme.typography.extraSmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(id = R.string.celsius, dailyForecast.minTemp),
                        style = ReplacementTheme.typography.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(id = R.string.min),
                        style = ReplacementTheme.typography.extraSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun DailyTempCardPreview() {
    WeatherForecastTheme {
        DailyTempCard(
            dailyForecast = DailyForecast(
                date = "16",
                minTemp = 20,
                maxTemp = 30,
                weatherType = WeatherType.CLEAR
            )
        )
    }
}