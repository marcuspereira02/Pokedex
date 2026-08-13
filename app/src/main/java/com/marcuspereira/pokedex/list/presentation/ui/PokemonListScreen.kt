package com.marcuspereira.pokedex.list.presentation.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.request.ImageRequest
import com.marcuspereira.pokedex.R
import com.marcuspereira.pokedex.common.utils.extractColorFromDrawable
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel

@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel, navController: NavHostController) {

    val listPokemon by viewModel.uiAllPokemon.collectAsState()

    PokemonListContent(
        pokemonListUiState = listPokemon,
        onClick = { itemClicked ->
            navController.navigate(route = "pokemonDetail/${itemClicked.id}")

        },
        onSearchClick = {
                navController.navigate(route = "pokemonSearchScreen")
        }
    )
}


@Composable
private fun PokemonListContent(
    pokemonListUiState: PokemonListUiState,
    onClick: (PokemonListUiData) -> Unit,
    onSearchClick: () -> Unit,

    ) {

    Column(modifier = Modifier.fillMaxSize()) {

        var query by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Icon(
                painter = painterResource(id = R.drawable.pokeball),
                contentDescription = "Pokeball Image",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.size(8.dp))
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                text = "Pokedex"
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Pokémon"
                )
            }
        }

        PokemonListContentGate(pokemonListUiState = pokemonListUiState, onClick = onClick)
    }
}

@Composable
private fun PokemonListContentGate(
    pokemonListUiState: PokemonListUiState,
    onClick: (PokemonListUiData) -> Unit
) {
    if (pokemonListUiState.isLoading) {
        CircularProgressIndicator()
    } else if (pokemonListUiState.isError) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = pokemonListUiState.errorMessage,
            fontWeight = FontWeight.SemiBold,
            color = Color.Red
        )
    } else {
        PokemonList(pokemonList = pokemonListUiState.list, onClick = onClick)
    }
}

@Composable
private fun PokemonList(
    pokemonList: List<PokemonListUiData>,
    onClick: (PokemonListUiData) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(pokemon = pokemon, onClick = onClick)
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: PokemonListUiData,
    onClick: (PokemonListUiData) -> Unit
) {

    var backgroundColor by remember { mutableStateOf(Color.White) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke(pokemon)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.image)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentDescription = "${pokemon.name} Image",
                modifier = Modifier.size(120.dp),
                onSuccess = { success ->
                    val drawable = success.result.drawable
                    backgroundColor = extractColorFromDrawable(drawable)
                }
            )

            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }

}