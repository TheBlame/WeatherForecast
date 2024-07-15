package dev.maxim_v.weather_app.data.network.source

import com.google.gson.JsonParseException
import dev.maxim_v.weather_app.data.network.api.ForecastApi
import dev.maxim_v.weather_app.data.network.api.ForecastRequest
import dev.maxim_v.weather_app.data.network.api.RequestResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ForecastSource @Inject constructor(private val forecastApi: ForecastApi) {

    suspend fun getForecast(request: ForecastRequest) =
        wrapRetrofitExceptions { forecastApi.loadForecast(
            latitude = request.latitude,
            longitude = request.longitude,
            currentArgs = request.currentArgs,
            hourlyArgs = request.hourlyArgs,
            dailyArgs = request.dailyArgs,
            unit = request.unit,
            forecastDays = request.days
        ) }

    private suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): RequestResult<T> {
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