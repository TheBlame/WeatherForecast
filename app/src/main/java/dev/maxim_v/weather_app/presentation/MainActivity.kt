package dev.maxim_v.weather_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.weather.ForecastScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var repo: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WeatherForecastTheme {
                ForecastScreen()
            }
        }
    }
}

