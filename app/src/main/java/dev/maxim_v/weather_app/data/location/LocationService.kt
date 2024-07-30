package dev.maxim_v.weather_app.data.location

import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.location.LocationListener
import android.location.LocationManager
import dev.maxim_v.weather_app.domain.exceptions.GpsException
import dev.maxim_v.weather_app.util.checkLocationPermissions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow

class LocationService(private val application: Application) {
    private val locationManager: LocationManager =
        application.getSystemService(LOCATION_SERVICE) as LocationManager

    fun getLocation() = callbackFlow {
        if (checkLocationPermissions(application) && locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            ) && locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            val locationListener =
                LocationListener { location ->
                    trySend(location)
                }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener
            )
            delay(5000L)
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener
            )
            awaitClose { locationManager.removeUpdates(locationListener) }
        } else {
            throw GpsException()
        }
    }
}