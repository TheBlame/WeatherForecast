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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
            Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                CurrentWeatherCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    currentWeather = currentState.data[CURRENT] as WeatherModel.CurrentSample
                )
                HourlyChart(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                        .horizontalScroll(rememberScrollState())
                        .height(300.dp)
                        .width(1800.dp),
                    data = (currentState.data[HOURLY] as WeatherModel.HourlySample).sample,
                    mainGraphLineColor = MaterialTheme.colorScheme.tertiary,
                    secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
                    secondaryGraphLineAlpha = 0.5f,
                    graphGradientColor = MaterialTheme.colorScheme.primary,
                    graphGradientAlpha = 0.5f,
                    valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onBackground),
                    timeTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
                DailyChart(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                        .horizontalScroll(rememberScrollState())
                        .height(200.dp)
                        .width(1000.dp),
                    data = (currentState.data[DAILY] as WeatherModel.DailySample).sample,
                    mainGraphLineColor = MaterialTheme.colorScheme.primary,
                    mainGraphLineWidth = 16.dp,
                    valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onBackground),
                    dateTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        MainScreenState.Error -> {}
        MainScreenState.Initial -> {}
        MainScreenState.Loading -> {}
    }

}