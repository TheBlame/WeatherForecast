package dev.maxim_v.weather_app.presentation

import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
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
import dev.maxim_v.weather_app.data.workers.ForecastWorker
import dev.maxim_v.weather_app.presentation.navigation.AppNavGraph
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.AppThemeState
import dev.maxim_v.weather_app.presentation.viewmodels.ForegroundWorkState
import dev.maxim_v.weather_app.presentation.viewmodels.MainActivityVM
import dev.maxim_v.weather_app.util.isDark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityVM: MainActivityVM by viewModels()
    private lateinit var workManager: WorkManager
    private lateinit var notificationManager: NotificationManager

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
                            Log.d("worker",
                                workManager.getWorkInfosForUniqueWork(ForecastWorker.NAME)
                                    .toString()
                            )
                            if (it.status.enabled) {
                                Log.d("worker", "enable")
                                workManager.enqueueUniquePeriodicWork(
                                    ForecastWorker.NAME,
                                    ExistingPeriodicWorkPolicy.REPLACE,
                                    ForecastWorker.periodicWorkRequest()
                                )
                                Log.d("worker",
                                    workManager.getWorkInfosForUniqueWork(ForecastWorker.NAME)
                                        .toString()
                                )
                            } else if (!it.status.enabled) {
                                Log.d("worker", "disable")
                                workManager.cancelUniqueWork(ForecastWorker.NAME)
                                ForecastWorker.cancelNotification(notificationManager)
                                Log.d("worker",
                                    workManager.getWorkInfosForUniqueWork(ForecastWorker.NAME)
                                        .toString()
                                )
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

