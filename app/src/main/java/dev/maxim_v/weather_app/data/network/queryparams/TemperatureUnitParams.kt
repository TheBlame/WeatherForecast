package dev.maxim_v.weather_app.data.network.queryparams

import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit

enum class TemperatureUnitParams(private val param: String) {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit");

    override fun toString(): String {
        return param
    }

    companion object {
        fun getFromPref(pref: UserPref): TemperatureUnitParams {
            return when (pref.tempUnit) {
                TemperatureUnit.CELSIUS -> CELSIUS
                TemperatureUnit.FAHRENHEIT -> FAHRENHEIT
            }
        }
    }
}