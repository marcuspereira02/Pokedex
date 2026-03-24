package com.marcuspereira.pokedex.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.marcuspereira.pokedex.common.utils.extractColorFromDrawable
import com.marcuspereira.pokedex.search.PokemonSearchViewModel

@Composable
fun PokemonSearchScreen(
    querySearch: String,
    navController: NavHostController,
    searchViewModel: PokemonSearchViewModel
) {

    val pokemon by searchViewModel.uiPokemon.collectAsState()

    val queryFormatted = querySearch.lowercase()

    LaunchedEffect(queryFormatted) {
        searchViewModel.fetchPokemon(queryFormatted)
    }


    PokemonSearchContent(
        navController, pokemon.data,
        querySearch
    ) { itemClicked ->
        navController.navigate(route ="pokemonDetail/${itemClicked?.id}")
    }
}

@Composable
private fun PokemonSearchContent(
    navController: NavController,
    pokemonSearchUiData: PokemonSearchUiData?,
    query: String,
    onClick: (PokemonSearchUiData?) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PokemonSearchHeader(
            navController,
            query
        )

        PokemonSearchCard(
            pokemonSearchUiData,
            onClick
        )
    }


}

private fun getTextColor(background: Color): Color {
    return if (background.luminance() < 0.5f) {
        Color.White
    } else {
        Color.Black
    }
}

@Composable
private fun PokemonSearchCard(
    pokemon: PokemonSearchUiData?,
    onClick: (PokemonSearchUiData?) -> Unit
) {

    var backgroundColor by remember { mutableStateOf(Color.White) }

    val name: String? = pokemon?.name?.replaceFirstChar { it.uppercase() }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke(pokemon)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon?.image)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentDescription = "${pokemon?.name} Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 16.dp),
                onSuccess = { success ->
                    val drawable = success.result.drawable
                    backgroundColor = extractColorFromDrawable(drawable)
                }
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = getTextColor(backgroundColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


                Text(
                    text = "#${pokemon?.id?.toString()?.padStart(3, '0') ?: ""}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = getTextColor(backgroundColor).copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun PokemonSearchHeader(
    navController: NavController,
    query: String
) {

    val queryFormatted = query.replaceFirstChar { it.uppercase() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button"
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = queryFormatted
        )
    }

}