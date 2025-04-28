package com.carlostorres.weatherapppositivo

import com.carlostorres.weatherapppositivo.domain.usecases.GetCitiesWeatherSavedUseCase
import com.carlostorres.weatherapppositivo.domain.usecases.GetWeatherFromCoordinatesUseCase
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.carlostorres.weatherapppositivo.utils.ConnectionStatus
import com.carlostorres.weatherapppositivo.utils.ConnectivityObserver
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test


class MainViewModelTest {

    @Test
    fun `initial state should be correct`() {

        val getWeatherUseCase = mockk<GetWeatherFromCoordinatesUseCase>()
        val getCitiesUseCase = mockk<GetCitiesWeatherSavedUseCase>()
        val connectivityObserver = mockk<ConnectivityObserver>()

        every { connectivityObserver.isConnected } returns MutableStateFlow(ConnectionStatus.Available)

        val viewModel = MainViewModel(getWeatherUseCase, getCitiesUseCase, connectivityObserver)

        assertFalse(viewModel.isLoading.value!!)
        assertNull(viewModel.weather.value)
        assertNull(viewModel.error.value)
        assertEquals(emptyList<WeatherModel>(), viewModel.citiesWeather.value)
        assertNull(viewModel.locationLatLng.value)

    }

}