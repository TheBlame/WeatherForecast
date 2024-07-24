package dev.maxim_v.weather_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.maxim_v.weather_app.presentation.navigation.AppNavGraph
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.AppThemeState
import dev.maxim_v.weather_app.presentation.viewmodels.AppThemeVM
import dev.maxim_v.weather_app.util.isDark

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeVM: AppThemeVM = hiltViewModel()
            val themeState by themeVM.appTheme.collectAsStateWithLifecycle()

            when (val s = themeState) {
                is AppThemeState.Ready -> {
                    WeatherForecastTheme(s.theme.isDark()) {
                        val navHostController = rememberNavController()
                        AppNavGraph(navHostController = navHostController)
                    }
                }

                AppThemeState.Initial -> {}
            }
        }
    }
}

