package dev.maxim_v.weather_app.data.network.dto

import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.domain.entity.enums.WeatherType

enum class WeatherCodeDto(val weatherType: WeatherType) {
    @SerializedName("0")
    CLEAR_SKY(WeatherType.CLEAR),
    @SerializedName("1")
    MAINLY_CLEAR(WeatherType.CLEAR),
    @SerializedName("2")
    PARTLY_CLOUDY(WeatherType.CLOUDY),
    @SerializedName("3")
    OVERCAST(WeatherType.CLOUDY),
    @SerializedName("45")
    FOG(WeatherType.CLOUDY),
    @SerializedName("48")
    RIME_FOG(WeatherType.CLOUDY),
    @SerializedName("51")
    LIGHT_DRIZZLE(WeatherType.RAIN),
    @SerializedName("53")
    MODERATE_DRIZZLE(WeatherType.RAIN),
    @SerializedName("55")
    DENSE_DRIZZLE(WeatherType.RAIN),
    @SerializedName("56")
    LIGHT_FREEZING_DRIZZLE(WeatherType.RAIN),
    @SerializedName("57")
    DENSE_FREEZING_DRIZZLE(WeatherType.RAIN),
    @SerializedName("61")
    SLIGHT_RAIN(WeatherType.RAIN),
    @SerializedName("63")
    MODERATE_RAIN(WeatherType.RAIN),
    @SerializedName("65")
    HEAVY_RAIN(WeatherType.RAIN),
    @SerializedName("66")
    LIGHT_FREEZING_RAIN(WeatherType.RAIN),
    @SerializedName("67")
    HEAVY_FREEZING_RAIN(WeatherType.RAIN),
    @SerializedName("71")
    SLIGHT_SNOW_FALL(WeatherType.SNOW),
    @SerializedName("73")
    MODERATE_SNOW_FALL(WeatherType.SNOW),
    @SerializedName("75")
    HEAVY_SNOW_FALL(WeatherType.SNOW),
    @SerializedName("77")
    SNOW_GRAINS(WeatherType.SNOW),
    @SerializedName("80")
    SLIGHT_RAIN_SHOWER(WeatherType.RAIN),
    @SerializedName("81")
    MODERATE_RAIN_SHOWER(WeatherType.RAIN),
    @SerializedName("82")
    VIOLENT_RAIN_SHOWER(WeatherType.RAIN),
    @SerializedName("85")
    SLIGHT_SNOW_SHOWER(WeatherType.SNOW),
    @SerializedName("86")
    HEAVY_SNOW_SHOWER(WeatherType.SNOW),
    @SerializedName("95")
    THUNDERSTORM(WeatherType.THUNDER),
    @SerializedName("96")
    THUNDERSTORM_WITH_SLIGHT_HAIL(WeatherType.THUNDER),
    @SerializedName("99")
    THUNDERSTORM_WITH_HEAVY_HAIL(WeatherType.THUNDER)
}