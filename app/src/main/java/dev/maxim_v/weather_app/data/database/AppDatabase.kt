package dev.maxim_v.weather_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.maxim_v.weather_app.data.database.dbmodels.CurrentForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.DailyForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.HourlyForecastDbModel

@Database(
    entities = [CurrentForecastDbModel::class, HourlyForecastDbModel::class, DailyForecastDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "main.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DB_NAME
            ).build()
    }

    abstract fun forecastDao(): ForecastDao
}