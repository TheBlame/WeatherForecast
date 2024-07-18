package dev.maxim_v.weather_app.data.location

import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.location.LocationListener
import android.location.LocationManager
import dev.maxim_v.weather_app.util.checkLocationPermissions
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class LocationService(private val application: Application) {
    private val locationManager: LocationManager =
        application.getSystemService(LOCATION_SERVICE) as LocationManager

    fun getLocation() = callbackFlow {
        if (checkLocationPermissions(application)) {
            val gpsLocationListener =
                LocationListener {
                    trySend(it)
                }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0f, gpsLocationListener
            )
            awaitClose { locationManager.removeUpdates(gpsLocationListener) }
        } else {
            cancel()
        }
    }
}