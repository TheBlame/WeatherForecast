package dev.maxim_v.weather_app.presentation.ui.screens.forecastScreen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.CurrentForecast
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.Direction
import dev.maxim_v.weather_app.util.selectIcon
import dev.maxim_v.weather_app.util.stringResource

@Composable
fun CurrentWeatherCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    currentForecast: CurrentForecast
) {
    Card(
        modifier = modifier.width(IntrinsicSize.Max),
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(

                        text = stringResource(id = R.string.temp_format, currentForecast.temp),
                        style = ReplacementTheme.typography.extraLarge,
                    )
                    Text(
                        text = stringResource(id = R.string.apparent, currentForecast.apparentTemp),
                        style = ReplacementTheme.typography.small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.size(104.dp),
                    painter = painterResource(id = selectIcon(currentForecast.weatherType)),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            IconBetweenText(
                text1 = stringResource(id = R.string.wind),
                text2 = String.format(
                    "%s %s, %s",

                    currentForecast.windSpeed,
                    currentForecast.windSpeedUnit.stringResource(),
                    currentForecast.windDirection.stringResource()
                ),
                icon = R.drawable.weather_windy,
                primaryTextColor = MaterialTheme.colorScheme.onSurface,
                secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            IconBetweenText(
                text1 = stringResource(id = R.string.humidity),
                text2 = stringResource(id = R.string.humidity_format, currentForecast.humidity),
                icon = R.drawable.water_percent,
                primaryTextColor = MaterialTheme.colorScheme.onSurface,
                secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IconBetweenText(
    text1: String,
    text2: String,
    icon: Int,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text1,
            style = ReplacementTheme.typography.small,
            color = primaryTextColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text2,
            style = ReplacementTheme.typography.small,
            color = secondaryTextColor
        )
    }
}

@Preview
@Composable
private fun CurrentWeatherCardPreview() {
    WeatherForecastTheme(darkTheme = false) {
        CurrentWeatherCard(
            modifier = Modifier.fillMaxWidth(),
            currentForecast = CurrentForecast(
                temp = "28",
                apparentTemp = "25",
                humidity = "30",
                windSpeed = "10",
                windDirection = Direction.SOUTH,
                precipitation = "0 mm",
                temperatureUnit = TemperatureUnit.CELSIUS,
                windSpeedUnit = WindSpeedUnit.MS,
                weatherType = WeatherType.CLEAR
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}