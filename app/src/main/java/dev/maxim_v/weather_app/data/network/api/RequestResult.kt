package dev.maxim_v.weather_app.data.network.api

sealed class RequestResult<out T> {
    data class Success<T>(val content: T) : RequestResult<T>()
    data object Error : RequestResult<Nothing>()
}