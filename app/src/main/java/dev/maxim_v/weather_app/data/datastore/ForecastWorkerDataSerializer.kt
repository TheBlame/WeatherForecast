package dev.maxim_v.weather_app.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object ForecastWorkerDataSerializer : Serializer<ForecastWorkerData> {
    override val defaultValue: ForecastWorkerData
        get() = ForecastWorkerData()

    override suspend fun readFrom(input: InputStream): ForecastWorkerData {
        return try {
            Json.decodeFromString(
                deserializer = ForecastWorkerData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ForecastWorkerData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = ForecastWorkerData.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}