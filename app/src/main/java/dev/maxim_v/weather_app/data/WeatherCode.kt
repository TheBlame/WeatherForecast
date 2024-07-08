package dev.maxim_v.weather_app.data

import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.domain.entity.WeatherType

enum class WeatherCode(val code: Int) {
    @SerializedName("0") CLEAR_SKY(0),
    @SerializedName("1") MAINLY_CLEAR(1),
    @SerializedName("2") PARTLY_CLOUDY(2),
    @SerializedName("3") OVERCAST(3),
    @SerializedName("45") FOG(45),
    @SerializedName("48") RIME_FOG(48),
    @SerializedName("51") LIGHT_DRIZZLE(51),
    @SerializedName("53") MODERATE_DRIZZLE(53),
    @SerializedName("55") DENSE_DRIZZLE(55),
    @SerializedName("56") LIGHT_FREEZING_DRIZZLE(56),
    @SerializedName("57") DENSE_FREEZING_DRIZZLE(57),
    @SerializedName("61") SLIGHT_RAIN(61),
    @SerializedName("63") MODERATE_RAIN(63),
    @SerializedName("65") HEAVY_RAIN(65),
    @SerializedName("66") LIGHT_FREEZING_RAIN(66),
    @SerializedName("67") HEAVY_FREEZING_RAIN(67),
    @SerializedName("71") SLIGHT_SNOW_FALL(71),
    @SerializedName("73") MODERATE_SNOW_FALL(73),
    @SerializedName("75") HEAVY_SNOW_FALL(75),
    @SerializedName("77") SNOW_GRAINS(77),
    @SerializedName("80") SLIGHT_RAIN_SHOWER(80),
    @SerializedName("81") MODERATE_RAIN_SHOWER(81),
    @SerializedName("82") VIOLENT_RAIN_SHOWER(82),
    @SerializedName("85") SLIGHT_SNOW_SHOWER(85),
    @SerializedName("86") HEAVY_SNOW_SHOWER(86),
    @SerializedName("95") THUNDERSTORM(95),
    @SerializedName("96") THUNDERSTORM_WITH_SLIGHT_HAIL(96),
    @SerializedName("99") THUNDERSTORM_WITH_HEAVY_HAIL(99);

    fun toWeatherType(): WeatherType {
        return when (this) {
            CLEAR_SKY, MAINLY_CLEAR -> WeatherType.CLEAR

            PARTLY_CLOUDY, OVERCAST, FOG, RIME_FOG -> WeatherType.CLOUDY

            LIGHT_DRIZZLE, MODERATE_DRIZZLE, DENSE_DRIZZLE, LIGHT_FREEZING_DRIZZLE,
            DENSE_FREEZING_DRIZZLE, SLIGHT_RAIN, MODERATE_RAIN, HEAVY_RAIN, LIGHT_FREEZING_RAIN,
            HEAVY_FREEZING_RAIN, SLIGHT_RAIN_SHOWER, MODERATE_RAIN_SHOWER, VIOLENT_RAIN_SHOWER
            -> WeatherType.RAIN

            SLIGHT_SNOW_FALL, MODERATE_SNOW_FALL, HEAVY_SNOW_FALL, SNOW_GRAINS, SLIGHT_SNOW_SHOWER,
            HEAVY_SNOW_SHOWER -> WeatherType.SNOW

            THUNDERSTORM, THUNDERSTORM_WITH_SLIGHT_HAIL, THUNDERSTORM_WITH_HEAVY_HAIL
            -> WeatherType.THUNDER
        }
    }
}