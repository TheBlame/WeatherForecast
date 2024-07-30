package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class ReverseGeocodingResponseDto(
    @SerializedName("city")
    val city: String?,
    @SerializedName("locality")
    val locality: String
)