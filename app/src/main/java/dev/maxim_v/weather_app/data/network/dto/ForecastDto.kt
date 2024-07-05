package dev.maxim_v.weather_app.data.network.dto


import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("current")
    val current: CurrentDto?,
    @SerializedName("current_units")
    val currentUnits: CurrentUnitsDto?,
    @SerializedName("daily")
    val daily: DailyDto?,
    @SerializedName("daily_units")
    val dailyUnits: DailyUnitsDto?,
    @SerializedName("hourly")
    val hourly: HourlyDto?,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnitsDto?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Long
)