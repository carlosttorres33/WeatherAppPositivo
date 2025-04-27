package com.carlostorres.weatherapppositivo.ui.components.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchText: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = searchText,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = "P. ej: Lima Peru")
        },
        label = {
            Text(text = "Buscar Lugar")
        },
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = Color.Black
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Black,
        ),
    )

}