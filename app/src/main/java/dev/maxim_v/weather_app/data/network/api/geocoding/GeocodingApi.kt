package dev.maxim_v.weather_app.data.network.api.geocoding

import dev.maxim_v.weather_app.data.network.dto.GeocodingResponseDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("search")
    suspend fun getLocationNames(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String,
        @Query("format") format: String = "json"
    ): GeocodingResponseDto

    companion object {
        private const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun create(): GeocodingApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GeocodingApi::class.java)
        }
    }
}
