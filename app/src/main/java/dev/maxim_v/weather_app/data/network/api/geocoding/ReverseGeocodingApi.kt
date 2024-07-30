package dev.maxim_v.weather_app.data.network.api.geocoding

import dev.maxim_v.weather_app.data.network.dto.ReverseGeocodingResponseDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeocodingApi {

    @GET("reverse-geocode-client")
    suspend fun getLocationName(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") language: String
    ): ReverseGeocodingResponseDto

    companion object {
        private const val BASE_URL = "https://api.bigdatacloud.net/data/"

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        fun create(): ReverseGeocodingApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ReverseGeocodingApi::class.java)
        }
    }
}