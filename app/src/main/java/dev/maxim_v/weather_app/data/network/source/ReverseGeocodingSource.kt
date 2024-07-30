package dev.maxim_v.weather_app.data.network.source

import androidx.compose.ui.text.intl.Locale
import dev.maxim_v.weather_app.data.network.api.geocoding.ReverseGeocodingApi
import javax.inject.Inject

class ReverseGeocodingSource @Inject constructor(
    private val reverseGeocodingApi: ReverseGeocodingApi
) : BaseNetworkSource() {

    suspend fun getLocationName(latitude: Double, longitude: Double) = wrapRetrofitExceptions {
        reverseGeocodingApi.getLocationName(
            latitude = latitude,
            longitude = longitude,
            language = Locale.current.language
        )
    }
}