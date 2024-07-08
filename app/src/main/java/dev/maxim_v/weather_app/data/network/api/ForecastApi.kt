package dev.maxim_v.weather_app.data.network.api

import dev.maxim_v.weather_app.data.network.dto.ForecastDto
import dev.maxim_v.weather_app.data.network.queryparams.Current
import dev.maxim_v.weather_app.data.network.queryparams.Daily
import dev.maxim_v.weather_app.data.network.queryparams.Hourly
import dev.maxim_v.weather_app.data.network.queryparams.TemperatureUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastApi {

    @GET("forecast")
    suspend fun loadForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentArgs: @JvmSuppressWildcards List<Current>?,
        @Query("hourly") hourlyArgs: @JvmSuppressWildcards List<Hourly>?,
        @Query("daily") dailyArgs: @JvmSuppressWildcards List<Daily>?,
        @Query("temperature_unit") unit: TemperatureUnit,
        @Query("timeformat") timeFormat:String = "unixtime",
        @Query("timezone") timeZone: String = "auto"
    ): ForecastDto

    companion object {

        private const val BASE_URL = "https://api.open-meteo.com/v1/"

        private val okHttpClient = OkHttpClient.Builder()
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