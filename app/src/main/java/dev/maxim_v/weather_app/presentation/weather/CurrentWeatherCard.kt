package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.selectIcon

@Composable
fun CurrentWeatherCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    currentWeather: WeatherModel.CurrentSample
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Max)
        ) {
            Text(
                text = currentWeather.location,
                style = ReplacementTheme.typography.large,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentWeather.time,
                style = ReplacementTheme.typography.large,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.celsius, currentWeather.temp),
                        style = ReplacementTheme.typography.extraLarge,
                    )
                    Text(
                        text = stringResource(id = R.string.apparent, currentWeather.apparentTemp),
                        style = ReplacementTheme.typography.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.size(96.dp),
                    painter = painterResource(id = selectIcon(currentWeather.weatherType)),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconWithText(
                    icon = R.drawable.water_percent,
                    text = stringResource(id = R.string.humidity, currentWeather.humidity)
                )
                IconWithText(
                    icon = R.drawable.weather_windy,
                    text = stringResource(
                        id = R.string.wind_speed,
                        currentWeather.windSpeed,
                        currentWeather.windDirection
                    )
                )
            }
        }
    }
}

@Composable
fun IconWithText(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = ReplacementTheme.typography.small,
        )
    }
}

@Preview
@Composable
private fun CurrentWeatherCardPreview() {
    WeatherForecastTheme(darkTheme = true) {
        CurrentWeatherCard(
            modifier = Modifier.fillMaxWidth(),
            currentWeather = WeatherModel.CurrentSample(
                location = "Location",
                time = "12:00",
                temp = "28",
                apparentTemp = "25",
                humidity = "30",
                windSpeed = "10",
                windDirection = "south",
                precipitation = "0 mm"
            )
        )
    }
}