package dev.maxim_v.weather_app.data.workers

import android.app.Activity.NOTIFICATION_SERVICE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.data.datastore.ForecastWorkerData
import dev.maxim_v.weather_app.domain.usecases.UpdateWidgetsDataUseCase
import dev.maxim_v.weather_app.util.createIconFromString
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class ForecastNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateWidgetsDataUseCase: UpdateWidgetsDataUseCase,
    private val forecastWorkerDs: DataStore<ForecastWorkerData>
) : CoroutineWorker(context, workerParameters) {

    private val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private fun showCurrentTempNotification(temp: String) {
        val icon = createIconFromString(context.getString(R.string.temp_format, temp))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CURRENT_FORECAST_CHANNEL_ID,
                CURRENT_FORECAST_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, CURRENT_FORECAST_CHANNEL_ID)
            .setOngoing(true)
            .setTimeoutAfter(NOTIFICATION_TIME_OUT)
            .setSmallIcon(icon)
            .setAutoCancel(false)
            .build()
        notificationManager.notify(CURRENT_TEMP_NOTIFICATION_ID, notification)
    }

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        try {
            updateWidgetsDataUseCase()
            showCurrentTempNotification(forecastWorkerDs.data.first().temp)
        } catch (_: Exception) {
        }

        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        this.workerParameters
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                LOADING_FORECAST_CHANNEL_ID,
                LOADING_FORECAST_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, LOADING_FORECAST_CHANNEL_ID)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                LOADING_FORECAST_NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(LOADING_FORECAST_NOTIFICATION_ID, notification)
        }
    }

    companion object {

        const val NAME = "forecast_worker"
        const val NOTIFICATION_TIME_OUT = 5400000L
        const val CURRENT_FORECAST_CHANNEL_ID = "current_temp"
        const val CURRENT_FORECAST_CHANNEL_NAME = "CurrentForecastChannel"
        const val LOADING_FORECAST_CHANNEL_ID = "loading_data"
        const val LOADING_FORECAST_CHANNEL_NAME = "LoadingChannel"
        const val CURRENT_TEMP_NOTIFICATION_ID = 1
        const val LOADING_FORECAST_NOTIFICATION_ID = 2

        fun periodicWorkRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<ForecastNotificationWorker>(
                MIN_PERIODIC_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS
            ).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()
        }

        fun cancelNotification(notificationManager: NotificationManager) {
            notificationManager.cancel(1)
        }
    }
}