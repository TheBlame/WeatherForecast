package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = viewModel()) {
    val screenState = viewModel.mainScreenState.collectAsState(MainScreenState.Initial)

    when (val currentState = screenState.value) {
        is MainScreenState.Success -> {
            Column(modifier = modifier) {
                CurrentWeatherCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    currentWeather = currentState.data[CURRENT] as WeatherModel.CurrentSample
                )
                LineChart(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .height(300.dp)
                        .width(1800.dp),
                    data = (currentState.data[HOURLY] as WeatherModel.HourlySample).sample,
                    mainGraphLineColor = MaterialTheme.colorScheme.tertiary,
                    secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
                    secondaryGraphLineAlpha = 0.5f,
                    graphGradientColor = MaterialTheme.colorScheme.primary,
                    graphGradientAlpha = 0.5f,
                    textColor = MaterialTheme.colorScheme.onBackground,
                    timeTextColor = MaterialTheme.colorScheme.onSurface,
                    valueTextStyle = ReplacementTheme.typography.small,
                    timeTextStyle = ReplacementTheme.typography.extraSmall
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = (currentState.data[DAILY] as WeatherModel.DailySample).sample,
                        key = { it.date }) { item: DailyForecast ->
                        DailyTempCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            dailyForecast = item
                        )
                    }
                }
            }
        }

        MainScreenState.Error -> {}
        MainScreenState.Initial -> {}
        MainScreenState.Loading -> {}
    }

}