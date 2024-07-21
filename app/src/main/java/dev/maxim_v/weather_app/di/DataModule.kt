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
import dev.maxim_v.weather_app.data.datastore.UserLocationSerializer
import dev.maxim_v.weather_app.data.datastore.UserPref
import dev.maxim_v.weather_app.data.datastore.UserPrefSerializer
import dev.maxim_v.weather_app.data.datastore.UserSavedLocation
import dev.maxim_v.weather_app.data.geocoder.GeocoderSource
import dev.maxim_v.weather_app.data.location.LocationService
import dev.maxim_v.weather_app.data.network.api.ForecastApi
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFS_FILE_NAME = "user_prefs.pb"
private const val USER_SAVED_LOCATION_FILE_NAME = "user_saved_location.pb"

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
                produceFile = { application.applicationContext.dataStoreFile(USER_PREFS_FILE_NAME) },
                corruptionHandler = null,
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            )
        }

        @Singleton
        @Provides
        fun provideUserSavedLocation(application: Application): DataStore<UserSavedLocation> {
            return DataStoreFactory.create(
                serializer = UserLocationSerializer,
                produceFile = {application.applicationContext.dataStoreFile(
                    USER_SAVED_LOCATION_FILE_NAME)},
                corruptionHandler = null,
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            )
        }

        @Singleton
        @Provides
        fun provideLocationService(application: Application): LocationService {
            return LocationService(application)
        }
    }
}