package dev.maxim_v.weather_app.data.geocoder

import android.app.Application
import android.location.Geocoder
import dev.maxim_v.weather_app.data.network.api.RequestResult
import dev.maxim_v.weather_app.data.network.source.GeocodingSource
import dev.maxim_v.weather_app.data.network.source.ReverseGeocodingSource
import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.domain.exceptions.GpsException
import dev.maxim_v.weather_app.domain.exceptions.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class AppGeocoder @Inject constructor(
    application: Application,
    private val geocodingSource: GeocodingSource,
    private val reverseGeocodingSource: ReverseGeocodingSource
) {
    private val geocoder = Geocoder(application)

    suspend fun getLocationName(latitude: Double, longitude: Double): String {
        if (Geocoder.isPresent()) {
            return withContext(Dispatchers.IO) {
                try {
                    geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.locality ?: ""
                } catch (e: IOException) {
                    throw NetworkException()
                } catch (e: IllegalArgumentException) {
                    throw GpsException()
                }
            }
        } else {
            val result = reverseGeocodingSource.getLocationName(latitude, longitude)
            if (result is RequestResult.Success) {
                return result.content.city ?: result.content.locality
            } else throw NetworkException()
        }

    }

    suspend fun getLocationFromName(name: String): List<SearchedLocation> {
        if (Geocoder.isPresent()) {
            return withContext(Dispatchers.IO) {
                if (!Geocoder.isPresent()) throw NetworkException()
                try {
                    geocoder.getFromLocationName(name, 10)?.mapToSearchedLocation() ?: listOf()
                } catch (e: IOException) {
                    throw NetworkException()
                } catch (e: IllegalArgumentException) {
                    throw NetworkException()
                }
            }
        } else {
            val result = geocodingSource.getLocations(name)
            if (result is RequestResult.Success) {
                return result.content.results.map {
                    SearchedLocation(
                        country = it.country ?: "",
                        city = it.name,
                        area = it.admin1 ?: "",
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            } else throw NetworkException()
        }
    }
}