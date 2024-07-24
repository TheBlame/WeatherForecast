package dev.maxim_v.weather_app.data.geocoder

import android.app.Application
import android.location.Address
import android.location.Geocoder
import dev.maxim_v.weather_app.domain.exceptions.GpsException
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class GeocoderSource(application: Application) {
    private val geocoder = Geocoder(application)

    suspend fun getLocationName(latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.locality ?: ""
            } catch (e: IOException) {
                throw NetworkException()
            } catch (e: IllegalArgumentException) {
                throw GpsException()
            }
        }
    }

    suspend fun getLocationFromName(name: String): List<Address>? {
        return withContext(Dispatchers.IO) {
            if (!Geocoder.isPresent()) throw NetworkException()
            try {
                geocoder.getFromLocationName(name, 10)
            } catch (e: IOException) {
                throw NetworkException()
            } catch (e: IllegalArgumentException) {
                throw NetworkException()
            }
        }
    }
}