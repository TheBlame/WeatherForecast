package dev.maxim_v.weather_app.data.network.queryparams

enum class TemperatureUnit(private val param: String) {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit");

    override fun toString(): String {
        return param
    }
}