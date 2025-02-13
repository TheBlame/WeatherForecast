package dev.maxim_v.weather_app.data.network.queryparams

enum class CurrentParams(private val param: String) {
    TEMPERATURE("temperature_2m"),
    HUMIDITY("relative_humidity_2m"),
    APPARENT("apparent_temperature"),
    CODE("weather_code"),
    WIND_SPEED("wind_speed_10m"),
    WIND_DIRECTION("wind_direction_10m");

    override fun toString(): String {
        return param
    }
}