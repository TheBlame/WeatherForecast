package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class GeocodingResultDto(
    @SerializedName("admin1")
    val admin1: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String
)