package dev.maxim_v.weather_app.domain.exceptions

abstract class AppException(message: String? = null, cause: Throwable? = null) :
    Exception(message, cause)