package com.carlostorres.weatherapppositivo.di

import android.content.Context
import com.carlostorres.weatherapppositivo.data.remote.RemoteWeatherDataSource
import com.carlostorres.weatherapppositivo.data.remote.WeatherService
import com.carlostorres.weatherapppositivo.data.repository.WeatherRepositoryImplementation
import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import com.carlostorres.weatherapppositivo.domain.usecases.GetWeatherFromCoordinatesUseCase
import com.carlostorres.weatherapppositivo.utils.ConnectivityObserver
import com.carlostorres.weatherapppositivo.utils.ConnectivityObserverImpl
import com.carlostorres.weatherapppositivo.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherService(
        retrofit: Retrofit
    ) : WeatherService = retrofit.create(WeatherService::class.java)

    @Singleton
    @Provides
    fun provideRemoteWeatherDataSource(
        @ApplicationContext context: Context,
        weatherService: WeatherService
    ) : RemoteWeatherDataSource = RemoteWeatherDataSource(
        context = context,
        weatherService = weatherService
    )

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteWeatherDataSource: RemoteWeatherDataSource
    ) : WeatherRepository = WeatherRepositoryImplementation(remoteWeatherDataSource)

    @Singleton
    @Provides
    fun provideGetWeatherFromCoordinatesUseCase(
        weatherRepository: WeatherRepository
    ) : GetWeatherFromCoordinatesUseCase = GetWeatherFromCoordinatesUseCase(weatherRepository)

    @Singleton
    @Provides
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ) : ConnectivityObserver = ConnectivityObserverImpl(context)

}