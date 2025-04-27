package com.carlostorres.weatherapppositivo.ui.components.compose

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.carlostorres.weatherapppositivo.presentation.MainEvents
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocalPlacesBSScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onItemClickListener: (WeatherModel) -> Unit,
    onMoveCameraPosition : (LatLng) -> Unit,
    setOfflineSearchedName : (String) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val savedPlaces = viewModel.citiesWeather.collectAsState()

    LaunchedEffect (Unit){
        viewModel.onEvent(
            MainEvents.GetCitiesWeatherSaved
        )
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText,
            onValueChange = {
                searchText = it
            },
            placeholder = {
                Text(text = "Buscar Lugar")
            }
        )

        if (savedPlaces.value.isEmpty()){
            Text(text = "Aun no hay lugares almacenados que mostrar Offline")
        }else{
            LazyColumn {
                items(savedPlaces.value){ place ->
                    LocalPlaceItem(
                        modifier = Modifier,
                        place = place,
                        onItemClickListener = {
                            Toast.makeText(context, place.name, Toast.LENGTH_SHORT).show()
                            viewModel.onEvent(
                                MainEvents.OfflineCitySelected(
                                    weather = place,
                                    setSearchText = {
                                        setOfflineSearchedName(place.name)
                                    }
                                )
                            )
                            onMoveCameraPosition(
                                LatLng(place.coordLat, place.coordLon)
                            )
                            onItemClickListener(it)
                        }
                    )

                }
            }
        }

    }

}

@Composable
fun LocalPlaceItem(
    modifier: Modifier = Modifier,
    place: WeatherModel,
    onItemClickListener: (WeatherModel) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .clickable {
                onItemClickListener(place)
            }
    ){
        Text(text = place.name, modifier = Modifier.align(Alignment.CenterStart))
    }

}