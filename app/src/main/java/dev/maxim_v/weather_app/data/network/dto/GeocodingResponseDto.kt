package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class GeocodingResponseDto(
    @SerializedName("results")
    val results: List<GeocodingResultDto>
)