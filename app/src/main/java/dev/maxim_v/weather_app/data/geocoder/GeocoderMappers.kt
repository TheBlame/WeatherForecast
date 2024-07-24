package dev.maxim_v.weather_app.data.geocoder

import android.location.Address
import dev.maxim_v.weather_app.domain.entity.SearchedLocation

fun Address.toSearchedLocation(): SearchedLocation {
    return SearchedLocation(
        country = this.countryName,
        city = this.locality ?: this.getAddressLine(0).takeWhile { it != ',' },
        area = this.adminArea,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun List<Address>.mapToSearchedLocation(): List<SearchedLocation> =
    filter { it.countryName != null && it.adminArea != null }.map {
        it.toSearchedLocation()
    }