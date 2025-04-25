package com.carlostorres.weatherapppositivo.data.remote

import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherFromCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
    ) : Response<WeatherResponse>

}