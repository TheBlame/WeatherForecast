package dev.maxim_v.weather_app.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maxim_v.weather_app.data.WeatherRepositoryImpl
import dev.maxim_v.weather_app.data.database.AppDatabase
import dev.maxim_v.weather_app.data.geocoder.GeocoderSource
import dev.maxim_v.weather_app.data.network.api.ForecastApi
import dev.maxim_v.weather_app.domain.repository.WeatherRepository
import javax.inject.Singleton

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
    }
}