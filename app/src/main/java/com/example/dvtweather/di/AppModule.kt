package com.example.dvtweather.di

import android.util.Config.DEBUG
import com.example.dvtweather.data.api.remote.WeatherAPI
import com.example.dvtweather.data.api.repositoriesImpl.WeatherRepositoryImpl
import com.example.dvtweather.data.domain.repositories.WeatherRepository
import com.example.dvtweather.util.AppConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor() : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideRetrofitClient():OkHttpClient = OkHttpClient()
            .newBuilder()
            .build()

    @Provides
    @Singleton
    fun provideWeatherAPI(client: OkHttpClient): WeatherAPI = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherAPI::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherAPI): WeatherRepository = WeatherRepositoryImpl(api)
}