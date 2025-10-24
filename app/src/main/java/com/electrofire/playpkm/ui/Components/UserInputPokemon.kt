package com.electrofire.playpkm.ui.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.electrofire.playpkm.ui.ViewModels.AutoPokeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun UserInputPokemon(title: String,
                     modifier: Modifier = Modifier,
                     text: String,
                     onTextChange: (String) -> Unit,
                     viewModel : AutoPokeViewModel = hiltViewModel(),
) {

    val sugerencias by viewModel.sugerencias.collectAsState()

    val listState = rememberLazyListState()

    val scrollFraction by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val visibleItems = listState.layoutInfo.visibleItemsInfo.size
            val firstIndex = listState.firstVisibleItemIndex
            if (totalItems <= visibleItems) 0f else firstIndex.toFloat() / (totalItems - visibleItems)
        }
    }

    val initialFraction = remember(sugerencias.size) {
        val visibleItems = minOf(4, sugerencias.size) // número máximo de items visibles
        if (sugerencias.isEmpty()) 0f else visibleItems.toFloat() / sugerencias.size
    }

    Column(modifier = Modifier.width(120.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Card(
            modifier = modifier.width(120.dp).height(55.dp).fillMaxSize(),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (title == "Potencia") {
                TextField(
                    value = text,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            onTextChange(input)
                        }
                    },
                    placeholder = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.outline,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            } else {
                TextField(
                    value = text,
                    onValueChange = {
                        onTextChange(it)
                        viewModel.onQueryChanged(it)
                    },
                    placeholder = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primary,    // Fondo cuando está enfocado
                        unfocusedContainerColor = MaterialTheme.colorScheme.outline,  // Fondo cuando NO está enfocado
                        focusedTextColor = MaterialTheme.colorScheme.secondary,       // Texto ingresado
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary, // Placeholder enfocado
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary, // Placeholder desenfocado
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

        }

        Spacer(Modifier.width(24.dp))

        AnimatedVisibility(
            visible = sugerencias.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = modifier.width(120.dp).wrapContentHeight().heightIn(max = 100.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ){
                Box(modifier = Modifier.wrapContentHeight()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(sugerencias) { pokemonEntity ->
                            Text(
                                text = pokemonEntity.nombre.uppercase(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onTextChange(pokemonEntity.nombre)
                                        viewModel.onQueryChanged("") // limpia las sugerencias
                                    }
                                    .padding(4.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    if (sugerencias.size >= 4) { // solo mostrar si hay suficiente contenido
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outline)
                                .align(Alignment.CenterEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(maxOf(scrollFraction, initialFraction).coerceIn(0f, 1f))
                                    .background(MaterialTheme.colorScheme.secondary)
                            )
                        }
                    }
                }
            }
        }
    }
}