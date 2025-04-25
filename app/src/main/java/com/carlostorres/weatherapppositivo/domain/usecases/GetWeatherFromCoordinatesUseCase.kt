package com.carlostorres.weatherapppositivo.domain.usecases

import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherFromCoordinatesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ) = weatherRepository.getWeatherFromCoordinates(latitude, longitude)

}