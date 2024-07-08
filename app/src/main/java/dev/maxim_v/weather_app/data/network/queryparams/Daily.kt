package dev.maxim_v.weather_app.data.network.queryparams

enum class Daily(private val param: String) {
    MIN_TEMPERATURE("temperature_2m_min"),
    MAX_TEMPERATURE("temperature_2m_max"),
    CODE("weather_code");

    override fun toString(): String {
        return param
    }
}