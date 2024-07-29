package dev.maxim_v.weather_app.data.workers

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.maxim_v.weather_app.domain.usecases.UpdateWidgetsDataUseCase
import dev.maxim_v.weather_app.widget.TimeWithTempWidget
import java.util.concurrent.TimeUnit

@HiltWorker
class ForecastWidgetWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateWidgetsDataUseCase: UpdateWidgetsDataUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            updateWidgetsDataUseCase()
        } catch (_: Exception) {
        }

        TimeWithTempWidget().updateAll(context)
        return Result.success()
    }

    companion object {

        const val NAME = "ForecastWidgetWorker"
        fun periodicWorkRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<ForecastNotificationWorker>(
                MIN_PERIODIC_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS
            ).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()
        }
    }
}