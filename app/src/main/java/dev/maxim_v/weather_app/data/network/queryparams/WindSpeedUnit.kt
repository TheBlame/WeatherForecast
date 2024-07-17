package dev.maxim_v.weather_app.data.network.queryparams

enum class WindSpeedUnit(private val param: String) {
    MS("ms"),
    KMH("kmh");

    override fun toString(): String {
        return param
    }
}