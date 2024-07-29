package dev.maxim_v.weather_app.presentation

import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import dev.maxim_v.weather_app.data.workers.ForecastNotificationWorker
import dev.maxim_v.weather_app.domain.usecases.UpdateWidgetsDataUseCase
import dev.maxim_v.weather_app.presentation.navigation.AppNavGraph
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.AppThemeState
import dev.maxim_v.weather_app.presentation.viewmodels.ForegroundWorkState
import dev.maxim_v.weather_app.presentation.viewmodels.MainActivityVM
import dev.maxim_v.weather_app.util.isDark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityVM: MainActivityVM by viewModels()
    private lateinit var workManager: WorkManager
    private lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var updateWidgetsDataUseCase: UpdateWidgetsDataUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)
        notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityVM.foregroundWorkState.collectLatest {
                    when (it) {
                        is ForegroundWorkState.Data -> {
                            try {
                                updateWidgetsDataUseCase(true)
                            } catch (_: Exception) {
                            }
                            if (it.status.enabled) {
                                workManager.enqueueUniquePeriodicWork(
                                    ForecastNotificationWorker.NAME,
                                    ExistingPeriodicWorkPolicy.REPLACE,
                                    ForecastNotificationWorker.periodicWorkRequest()
                                )

                            } else if (!it.status.enabled) {
                                workManager.cancelUniqueWork(ForecastNotificationWorker.NAME)
                                ForecastNotificationWorker.cancelNotification(notificationManager)
                            }
                        }

                        ForegroundWorkState.Initial -> {}
                    }
                }
            }
        }

        setContent {
            val themeState by mainActivityVM.appThemeState.collectAsStateWithLifecycle()

            when (val s = themeState) {
                is AppThemeState.Data -> {
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

