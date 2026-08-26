package com.electrofire.playpkm.ui.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.ViewModels.common.AutoPokeViewModelDos

@Composable
fun UserInputPokemonDos(
    title: String,
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    viewModel: AutoPokeViewModelDos = hiltViewModel(),
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

    Column(modifier = Modifier.width(200.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        if (title == "Potencia") {
            OutlinedTextField(
                value = text,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        onTextChange(input)
                    }
                },
                placeholder = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = MaterialTheme.typography.titleLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(0.6f),
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                    focusedPlaceholderColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.width(200.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

        } else {
            Box(modifier = Modifier.width(200.dp)) {
                OutlinedTextField(
                    value = text.uppercase(),
                    onValueChange = {
                        onTextChange(it)
                        viewModel.onQueryChanged(it)
                    },
                    placeholder = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = MaterialTheme.typography.titleLarge,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary.copy(0.6f),
                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (sugerencias.isNotEmpty() && title != "Habilidad") {
                    Icon(
                        painter = painterResource(id = R.drawable.caretabajo),
                        contentDescription = "Más sugerencias",
                        tint = Color(0xFF00C853),
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                            .offset(y = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        AnimatedVisibility(
            visible = sugerencias.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = modifier
                    .width(200.dp)
                    .wrapContentHeight()
                    .heightIn(max = 100.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.tertiary)

            ) {
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
                                        onTextChange(pokemonEntity.nombre.uppercase())
                                        viewModel.onQueryChanged("") // limpia las sugerencias
                                    }
                                    .padding(4.dp),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 12.sp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    if (sugerencias.size >= 4) { // solo mostrar si hay suficiente contenido
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(Color.Transparent)
                                .align(Alignment.CenterEnd)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(
                                        maxOf(scrollFraction, initialFraction).coerceIn(
                                            0f,
                                            1f
                                        )
                                    )
                                    .background(Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}