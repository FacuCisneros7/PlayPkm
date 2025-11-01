package com.electrofire.playpkm.ui.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.electrofire.playpkm.Data.PokemonApi
import com.electrofire.playpkm.R
import com.electrofire.playpkm.ui.CardItems.PokemonApiCard
import com.electrofire.playpkm.ui.Components.ConfirmButton
import com.electrofire.playpkm.ui.Components.GradientBackground
import com.electrofire.playpkm.ui.Components.HabilityCard
import com.electrofire.playpkm.ui.Components.Loading
import com.electrofire.playpkm.ui.Components.LoserCard
import com.electrofire.playpkm.ui.Components.WinCard
import com.electrofire.playpkm.ui.ViewModels.EightGameViewModel
import com.electrofire.playpkm.ui.ViewModels.HomeStatsViewModel
import com.electrofire.playpkm.ui.ViewModels.SeventhGameViewModel
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaEightGame
import com.electrofire.playpkm.ui.ViewModels.verificarRespuestaSeventhGame
import kotlin.collections.get

@Composable
fun EightGame(navController: NavController, viewModel: EightGameViewModel = hiltViewModel(), statsViewModel: HomeStatsViewModel) {

    val state by viewModel.state.collectAsState()

    var selectedPokemon by remember { mutableStateOf<PokemonApi?>(null) }

    var respondido by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize()
    ) {

        GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.pokemons.isNotEmpty() && state.abilityName != null) {

                if (!respondido) {

                    Image(
                        painter = painterResource(id = R.drawable.octavojuego),
                        contentDescription = null,
                        modifier = Modifier.height(30.dp).wrapContentWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Habilidad: ${state.abilityName}".replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PokemonApiCard(
                            pokemon = state.pokemons[0],
                            onSelected = state.pokemons[0] == selectedPokemon,
                            onClick = { selectedPokemon = state.pokemons[0] },
                            modifier = Modifier.size(130.dp)
                        )

                        Spacer(modifier = Modifier.width(32.dp))

                        PokemonApiCard(
                            pokemon = state.pokemons[1],
                            onSelected = state.pokemons[1] == selectedPokemon,
                            onClick = { selectedPokemon = state.pokemons[1] },
                            modifier = Modifier.size(130.dp)
                        )

                    }

                    Spacer(modifier = Modifier.width(26.dp))

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PokemonApiCard(
                            pokemon = state.pokemons[2],
                            onSelected = state.pokemons[2] == selectedPokemon,
                            onClick = { selectedPokemon = state.pokemons[2] },
                            modifier = Modifier.size(130.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(26.dp))

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PokemonApiCard(
                            pokemon = state.pokemons[3],
                            onSelected = state.pokemons[3] == selectedPokemon,
                            onClick = { selectedPokemon = state.pokemons[3] },
                            modifier = Modifier.size(130.dp)
                        )

                        Spacer(modifier = Modifier.width(32.dp))

                        PokemonApiCard(
                            pokemon = state.pokemons[4],
                            onSelected = state.pokemons[4] == selectedPokemon,
                            onClick = { selectedPokemon = state.pokemons[4] },
                            modifier = Modifier.size(130.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    ConfirmButton(
                        onConfirm = { respondido = true },
                        enabled = selectedPokemon != null
                    )
                } else {

                    Spacer(Modifier.height(32.dp))

                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .height(180.dp)
                            .padding(8.dp),
                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.onSurface),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = state.impostor!!.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(150.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(Modifier.height(64.dp))

                    HabilityCard(pokemonActual = state.impostor)

                    Spacer(Modifier.height(64.dp))

                    if (verificarRespuestaEightGame(
                            correctPokemon = state.impostor!!.name,
                            choisePokemon = selectedPokemon!!.name
                        )
                    ) {
                        WinCard(onButtonClick = { navController.navigate("home") })
                    } else {
                        LoserCard(onButtonClick = { navController.navigate("home") })
                    }

                    LaunchedEffect(Unit) {
                        if (verificarRespuestaEightGame(
                                correctPokemon = state.impostor!!.name,
                                choisePokemon = selectedPokemon!!.name
                            )
                        ) {
                            statsViewModel.registrarVictoria()
                        } else {
                            statsViewModel.registrarDerrota()
                        }
                    }

                }
            } else{
                Spacer(Modifier.height(200.dp))
                Loading()
            }
        }
    }
}

