package com.carlostorres.weatherapppositivo.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.weatherapppositivo.domain.usecases.GetWeatherFromCoordinatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeatherFromCoordinatesUseCase: GetWeatherFromCoordinatesUseCase
) : ViewModel() {

    private val _state = MutableLiveData(MainState())
    val state: LiveData<MainState> = _state

    fun onEvent(event: MainEvents) {
        when (event) {
            is MainEvents.OnPlaceSelected -> {
                _state.postValue(
                    _state.value?.copy(
                        latitude = event.latitude,
                        longitude = event.longitude
                    )
                )
            }

            is MainEvents.OnSearchClicked -> {
                if (_state.value?.latitude != null || _state.value?.longitude != null) {
                    getWeatherFromCoordinates(_state.value?.latitude!!, _state.value?.longitude!!)
                }
            }
        }
    }

    private fun getWeatherFromCoordinates(
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch(Dispatchers.IO) {

        _state.postValue(MainState(isLoading = true, weather = null, error = null))

        try {

            val weatherResponse = getWeatherFromCoordinatesUseCase(latitude, longitude)

            if (weatherResponse != null) {
                _state.postValue(
                    MainState(
                        isLoading = false,
                        weather = weatherResponse,
                        error = null
                    )
                )
                Log.d("MainViewModel", "Weather data fetched successfully")
            } else {
                _state.postValue(
                    MainState(
                        isLoading = false,
                        weather = null,
                        error = "Error fetching weather data"
                    )
                )
                Log.e("MainViewModel", "Error fetching weather data")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainViewModel", "Error fetching weather data: ${e.message}")
            _state.postValue(MainState(isLoading = true, weather = null, error = e.message))
        }

    }

}