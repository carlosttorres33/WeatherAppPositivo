package com.carlostorres.weatherapppositivo.domain.usecases

import com.carlostorres.weatherapppositivo.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCitiesWeatherSavedUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke() = weatherRepository.getCitiesWeatherSaved()

}