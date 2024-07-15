package dev.maxim_v.weather_app.data.geocoder

import android.app.Application
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeocoderSource(application: Application) {
    private val geocoder = Geocoder(application)

    suspend fun getLocationName(latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO) {
            geocoder.getFromLocation(latitude, longitude, 1)?.first()?.locality ?: ""
        }
    }
}