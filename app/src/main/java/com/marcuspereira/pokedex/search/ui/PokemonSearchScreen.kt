package com.marcuspereira.pokedex.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.marcuspereira.pokedex.R
import com.marcuspereira.pokedex.common.utils.extractColorFromDrawable
import com.marcuspereira.pokedex.common.utils.getTextColor
import com.marcuspereira.pokedex.components.ERSearchBar
import com.marcuspereira.pokedex.components.RetryErrorContent
import com.marcuspereira.pokedex.components.TryAgainButton
import com.marcuspereira.pokedex.search.PokemonSearchViewModel

@Composable
fun PokemonSearchScreen(
    navController: NavHostController,
    searchViewModel: PokemonSearchViewModel
) {

    val pokemon by searchViewModel.uiPokemon.collectAsState()

    var query by rememberSaveable {
        mutableStateOf("")
    }


    Column(modifier = Modifier.fillMaxSize()) {


        PokemonSearchHeader(
            navController = navController,
        )

        Spacer(
            modifier = Modifier.size(8.dp)
        )

        SearchSession(
            query = query,
            onValueChange = { newQuery ->
                query = newQuery
                searchViewModel.onQueryChanged(newQuery)
            },
            onSearchClicked = {}
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        when {
            pokemon.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            pokemon.isError -> {
                RetryErrorContent (
                    pokemon.errorMessage,
                    onRetry = { searchViewModel.retryLoadPokemonList() }
                )
            }

            query.isBlank() -> {
                SearchEmptyContent()
            }

            pokemon.data.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "No Pokémon found.",
                    )
                }
            }

            else -> {

                LazyColumn {
                    items(
                        items = pokemon.data,
                    ) { item ->
                        PokemonSearchContent(
                            pokemonSearchUiData = item,
                            onClick = { itemClicked ->
                                itemClicked?.id?.let { id ->
                                    navController.navigate(route = "pokemonDetail/$id")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonSearchContent(
    pokemonSearchUiData: PokemonSearchUiData?,
    onClick: (PokemonSearchUiData?) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        PokemonSearchCard(
            pokemonSearchUiData,
            onClick
        )
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
) {

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
            text = "Search Pokémon"
        )
    }
}

@Composable
fun SearchSession(
    query: String,
    onValueChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    ERSearchBar(
        query = query,
        placeHolder = "Search by name",
        onValueChange = onValueChange,
        onSearchClicked = {
            onSearchClicked.invoke(query)
        }
    )
}

@Composable
private fun SearchEmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Start typing to search",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

