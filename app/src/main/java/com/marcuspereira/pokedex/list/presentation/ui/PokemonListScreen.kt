package com.marcuspereira.pokedex.list.presentation.ui

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import com.marcuspereira.pokedex.R
import com.marcuspereira.pokedex.components.ERSearchBar
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel

@Composable
fun PokemonListScreen(viewModel: PokemonListViewModel) {

    val listPokemon by viewModel.uiAllPokemon.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        PokemonListContent(
            pokemonListUiState = listPokemon,
            onClick = {

            },
            onSearchClicked = { query ->
                val tempCleanQuery = query.trim()
                if (tempCleanQuery.isNotEmpty()) {

                }

            }

        )
    }

}

@Composable
private fun PokemonListContent(
    pokemonListUiState: PokemonListUiState,
    onClick: (PokemonListUiData) -> Unit,
    onSearchClicked: (String) -> Unit,

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
        }

        SearchSession(
            query = query,
            onValueChange = {
                query = it
            },
            onSearchClicked = onSearchClicked
        )
        PokemonListContentGate(pokemonListUiState = pokemonListUiState, onClick = onClick)

    }
}

@Composable
private fun SearchSession(
    query: String,
    onValueChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    ERSearchBar(
        query = query,
        placeHolder = "Find a Pokémon",
        onValueChange = onValueChange,
        onSearchClicked = {
            onSearchClicked.invoke(query)
        }
    )
}

@Composable
private fun PokemonListContentGate(
    pokemonListUiState: PokemonListUiState,
    onClick: (PokemonListUiData) -> Unit
) {
    if (pokemonListUiState.isLoading) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Loading...",
            fontWeight = FontWeight.SemiBold
        )
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
private fun PokemonCard(pokemon: PokemonListUiData, onClick: (PokemonListUiData) -> Unit) {

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

private fun extractColorFromDrawable(drawable: Drawable): Color {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val palette = Palette.from(bitmap).generate()

    val swatch = palette.dominantSwatch ?: palette.vibrantSwatch
    return swatch?.let { Color(it.rgb) } ?: Color.LightGray
}
