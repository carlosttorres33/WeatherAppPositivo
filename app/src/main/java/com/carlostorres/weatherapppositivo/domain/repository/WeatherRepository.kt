package com.carlostorres.weatherapppositivo.domain.repository

import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse

interface WeatherRepository {

    suspend fun getWeatherFromCoordinates(latitude: Double, longitude: Double) : WeatherResponse?

}
