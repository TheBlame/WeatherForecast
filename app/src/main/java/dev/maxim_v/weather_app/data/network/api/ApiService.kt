package dev.maxim_v.weather_app.data.network.api

import dev.maxim_v.weather_app.data.network.dto.ForecastDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast")
    suspend fun loadForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") currentArgs: List<String>?,
        @Query("hourly") hourlyArgs: List<String>?,
        @Query("daily") dailyArgs: List<String>?,
        @Query("timeformat") timeFormat:String,
        @Query("timezone") timeZone: String
    ): ForecastDto

    companion object {

        private const val BASE_URL = "https://api.open-meteo.com/v1/"

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun create(): ApiService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }
    }
}