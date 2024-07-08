package dev.maxim_v.weather_app.data.network.queryparams

enum class Hourly(private val param: String) {
    TEMPERATURE("temperature_2m"),
    CODE("weather_code");

    override fun toString(): String {
        return param
    }
}