package dev.maxim_v.weather_app.data.network.api

import dev.maxim_v.weather_app.data.network.dto.ForecastDto
import dev.maxim_v.weather_app.data.network.queryparams.CurrentParams
import dev.maxim_v.weather_app.data.network.queryparams.DailyParams
import dev.maxim_v.weather_app.data.network.queryparams.HourlyParams
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnitParams
import dev.maxim_v.weather_app.data.network.queryparams.WindSpeedUnitParams
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ForecastApi {

    @GET("forecast")
    suspend fun loadForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentParams: @JvmSuppressWildcards List<CurrentParams>?,
        @Query("hourly") hourlyParams: @JvmSuppressWildcards List<HourlyParams>?,
        @Query("daily") dailyParams: @JvmSuppressWildcards List<DailyParams>?,
        @Query("temperature_unit") tempUnit: TemperatureUnitParams,
        @Query("wind_speed_unit") windSpeedUnitParam: WindSpeedUnitParams,
        @Query("timeformat") timeFormat: String = "unixtime",
        @Query("timezone") timeZone: String = "auto",
        @Query("forecast_days") forecastDays: Int
    ): ForecastDto

    companion object {

        private const val BASE_URL = "https://api.open-meteo.com/v1/"

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Change it as per your requirement
            .readTimeout(30, TimeUnit.SECONDS)// Change it as per your requirement
            .writeTimeout(30, TimeUnit.SECONDS)// Change it as per your requirement
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun create(): ForecastApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ForecastApi::class.java)
        }
    }
}