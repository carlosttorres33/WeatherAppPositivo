package com.carlostorres.weatherapppositivo.ui.components.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.weatherapppositivo.presentation.MainEvents
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.carlostorres.weatherapppositivo.presentation.model.WeatherModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocalPlacesBSScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onItemClickListener: (WeatherModel) -> Unit,
    onMoveCameraPosition: (LatLng) -> Unit,
    setOfflineSearchedName: (String) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val savedPlaces = viewModel.citiesWeather.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            MainEvents.GetCitiesWeatherSaved
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
    ) {

        SearchTextField(
            modifier = Modifier.fillMaxWidth(),
            searchText = searchText
        ) {
            searchText = it
        }

        if (savedPlaces.value.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Aún no hay lugares almacenados que mostrar Offline",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                items(savedPlaces.value) { place ->
                    if (place.name.contains(searchText, ignoreCase = true)) {
                        LocalPlaceItem(
                            modifier = Modifier.padding(vertical = 6.dp),
                            place = place,
                            onItemClickListener = {
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
}