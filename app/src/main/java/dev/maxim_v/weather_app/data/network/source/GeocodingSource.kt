package dev.maxim_v.weather_app.data.network.source

import androidx.compose.ui.text.intl.Locale
import dev.maxim_v.weather_app.data.network.api.geocoding.GeocodingApi
import javax.inject.Inject

class GeocodingSource @Inject constructor(
    private val geocodingApi: GeocodingApi
) : BaseNetworkSource() {

    suspend fun getLocations(name: String) = wrapRetrofitExceptions {
        geocodingApi.getLocationNames(
            name = name,
            language = Locale.current.language
        )
    }
}