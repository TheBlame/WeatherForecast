package dev.maxim_v.weather_app.widget

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.datastore.core.DataStore
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.unit.ColorProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.data.datastore.ForecastWorkerData
import dev.maxim_v.weather_app.data.workers.ForecastWidgetWorker
import dev.maxim_v.weather_app.presentation.MainActivity
import dev.maxim_v.weather_app.util.selectIcon

class TimeWithTempWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlanceEntryPoint {
        fun getDs(): DataStore<ForecastWorkerData>
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val hiltEntrypoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            GlanceEntryPoint::class.java
        )
        val forecastWorkerDs = hiltEntrypoint.getDs()
        val mClockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
        mClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ForecastWidgetWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            ForecastWidgetWorker.periodicWorkRequest()
        )

        provideContent {
            Content(forecastWorkerDs, onClick = { startActivity(context, mClockIntent, null) })
        }

    }

    @Composable
    fun Content(ds: DataStore<ForecastWorkerData>, onClick: (Unit) -> Unit) {
        val data by ds.data.collectAsState(ForecastWorkerData())
        val packageName = LocalContext.current.packageName
        val context = LocalContext.current
        val size = LocalSize.current
        AndroidRemoteViews(
            modifier = GlanceModifier.fillMaxSize()
                .padding(horizontal = 8.dp)
                .clickable(R.color.transparent) {
                onClick(Unit)
            },
            remoteViews = RemoteViews(packageName, R.layout.widget_container),
            containerViewId = R.id.widget_temp_container
        ) {
            Text(
                modifier = GlanceModifier
                    .clickable(actionStartActivity<MainActivity>(), R.color.transparent),
                text = context.getString(R.string.temp_format, data.temp),
                style = TextDefaults.defaultTextStyle.copy(
                    color = ColorProvider(Color.White),
                    fontSize = if (size.width >= 240.dp) 32.sp else 24.sp
                )
            )
            Image(
                modifier = GlanceModifier
                    .size(if (size.width >= 240.dp) 32.dp else 24.dp)
                    .clickable(actionStartActivity<MainActivity>(), R.color.transparent),
                provider = ImageProvider(selectIcon(data.weatherType)),
                colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                contentDescription = null
            )
        }
    }
}

