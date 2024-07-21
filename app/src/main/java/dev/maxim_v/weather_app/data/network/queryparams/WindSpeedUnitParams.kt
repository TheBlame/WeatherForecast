package dev.maxim_v.weather_app.data.network.queryparams

import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit

enum class WindSpeedUnitParams(private val param: String) {
    MS("ms"),
    KMH("kmh");

    override fun toString(): String {
        return param
    }

    companion object {
        fun getFromPref(pref: UserPref): WindSpeedUnitParams {
            return when (pref.windSpeedUnit) {
                WindSpeedUnit.MS -> MS
                WindSpeedUnit.KMH -> KMH
            }
        }
    }
}