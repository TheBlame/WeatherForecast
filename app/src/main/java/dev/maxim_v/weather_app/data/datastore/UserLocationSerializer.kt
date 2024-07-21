package dev.maxim_v.weather_app.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserLocationSerializer : Serializer<UserSavedLocation> {
    override val defaultValue: UserSavedLocation
        get() = UserSavedLocation()

    override suspend fun readFrom(input: InputStream): UserSavedLocation {
        return try {
            Json.decodeFromString(
                deserializer = UserSavedLocation.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSavedLocation, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserSavedLocation.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}