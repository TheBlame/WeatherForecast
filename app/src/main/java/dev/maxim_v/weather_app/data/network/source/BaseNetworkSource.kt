package dev.maxim_v.weather_app.data.network.source

import com.google.gson.JsonParseException
import dev.maxim_v.weather_app.data.network.api.RequestResult
import retrofit2.HttpException
import java.io.IOException

abstract class BaseNetworkSource {

    protected suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): RequestResult<T> {
        return try {
            val result = block()
            RequestResult.Success(result)
        } catch (e: JsonParseException) {
            return RequestResult.Error
        } catch (e: HttpException) {
            return RequestResult.Error
        } catch (e: IOException) {
            return RequestResult.Error
        }
    }
}