package dev.maxim_v.weather_app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.maxim_v.weather_app.data.database.dbmodels.CurrentForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.DailyForecastDbModel
import dev.maxim_v.weather_app.data.database.dbmodels.HourlyForecastDbModel

private const val ONE_DAY_OFFSET = 86400L

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentForecast(currentForecast: CurrentForecastDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecast: List<HourlyForecastDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(dailyForecast: List<DailyForecastDbModel>)

    @Query("SELECT * FROM current_forecast LIMIT 1")
    suspend fun getCurrentForecast(): CurrentForecastDbModel?

    @Query("SELECT * FROM hourly_forecast WHERE timestamp > :time LIMIT 24")
    suspend fun getHourlyForecast(time: Long): List<HourlyForecastDbModel>

    @Query("DELETE FROM hourly_forecast")
    suspend fun clearHourlyForecast()

    @Query("SELECT * FROM daily_forecast LIMIT 14")
    suspend fun getDailyForecast(): List<DailyForecastDbModel>

    @Query("DELETE FROM daily_forecast")
    suspend fun clearDailyForecast()
}