package dev.maxim_v.weather_app.domain.exceptions

class DatabaseException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause) {
    constructor(cause: Throwable?) : this(null, cause)
}