package com.carlostorres.weatherapppositivo.presentation

import com.carlostorres.weatherapppositivo.data.remote.model.WeatherResponseDto

data class MainState(
    val isLoading: Boolean = false,
    val weather: WeatherResponseDto? = null,
    val error: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
