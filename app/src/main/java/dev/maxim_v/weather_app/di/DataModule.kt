package dev.maxim_v.weather_app.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maxim_v.weather_app.data.WeatherRepositoryImpl
import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.data.datastore.UserPrefSerializer
import dev.maxim_v.weather_app.data.geocoder.GeocoderSource
import dev.maxim_v.weather_app.data.network.api.ForecastApi
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindWeatherRepositoryImp(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @Singleton
        @Provides
        fun provideDb(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }

        @Singleton
        @Provides
        fun provideForecastApi(): ForecastApi {
            return ForecastApi.create()
        }

        @Singleton
        @Provides
        fun provideGeocoder(application: Application): GeocoderSource {
            return GeocoderSource(application)
        }

        @Singleton
        @Provides
        fun providePref(application: Application): DataStore<UserPref> {
            return DataStoreFactory.create(
                serializer = UserPrefSerializer,
                produceFile = { application.applicationContext.dataStoreFile(DATA_STORE_FILE_NAME) },
                corruptionHandler = null,
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            )
        }
    }
}