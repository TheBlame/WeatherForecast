package dev.maxim_v.weather_app.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object UserPrefSerializer : Serializer<UserPref> {
    override val defaultValue: UserPref
        get() = UserPref()

    override suspend fun readFrom(input: InputStream): UserPref {
        return try {
            Json.decodeFromString(
                deserializer = UserPref.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPref, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserPref.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}