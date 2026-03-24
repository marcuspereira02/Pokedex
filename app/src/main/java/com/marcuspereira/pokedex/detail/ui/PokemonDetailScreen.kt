package com.marcuspereira.pokedex.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.luminance
import coil.request.ImageRequest
import com.marcuspereira.pokedex.common.utils.extractColorFromDrawable
import com.marcuspereira.pokedex.detail.PokemonDetailViewModel

@Composable
fun PokemonDetailScreen(
    id: String,
    viewModel: PokemonDetailViewModel,
    navController: NavController
) {

    val pokemon by viewModel.uiPokemon.collectAsState()

    LaunchedEffect(id) {
        viewModel.fetchPokemonDetail(id)
    }

    val name: String? = pokemon.data?.name
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }


    when {
        pokemon.isLoading -> {
            Column {
                DetailHeader(
                    navController = navController,
                    viewModel = viewModel,
                    title = "LOADING!"
                )

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Loading...",
                    fontSize = 16.sp
                )
            }
        }

        pokemon.isError -> {
            Column {
                DetailHeader(
                    navController = navController,
                    viewModel = viewModel,
                    title = "ERROR!"
                )

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = pokemon.errorMessage ?: "",
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }
        }

        pokemon.data != null -> {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
            ) {

                DetailHeader(
                    navController = navController,
                    viewModel = viewModel,
                    title = name.toString()
                )
                PokemonDetailContent(
                    pokemon.data,
                    name.toString()
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(
    pokemon: PokemonDetailUiData?,
    name: String
) {


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        PokemonCard(pokemon)

        Text(
            modifier = Modifier.padding(16.dp),
            text = name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        ChipsTypes(pokemon)

        Row {

            PokemonMeasurements(
                pokemon?.weight ?: 0,
                "Weight"
            )

            Spacer(modifier = Modifier.size(24.dp))

            PokemonMeasurements(
                pokemon?.height ?: 0,
                "Height"
            )
        }

        StatusBars(pokemon)
    }

}

@Composable
private fun StatusBars(
    pokemon: PokemonDetailUiData?,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = "Base Stats",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        pokemon?.stats?.forEach { stat ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = abbreviateStatName(stat.name))
                Spacer(Modifier.size(8.dp))
                LinearProgressIndicator(
                    progress = stat.value / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}

@Composable
private fun PokemonMeasurements(value: Int, title: String) {

    val formattedValue = value / 10.0

    val measure = if (title == "Weight") {
        "KG"
    } else {
        "M"
    }

    Column(
        modifier = Modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format("%.1f %s", formattedValue, measure),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = title,
            fontSize = 14.sp
        )
    }

}

@Composable
private fun ChipsTypes(pokemon: PokemonDetailUiData?) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        pokemon?.types?.forEach { type ->

            val background = getTypeColor(type)
            val textColor = getTextColor(background)

            Surface(
                shape = RoundedCornerShape(50),
                color = background
            ) {
                Text(
                    text = type.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }

        }
    }

}

@Composable
private fun PokemonCard(pokemon: PokemonDetailUiData?) {

    var backgroundColor by remember { mutableStateOf(Color.White) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
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
                    .data(pokemon?.image)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentDescription = "${pokemon?.name} Image",
                modifier = Modifier.size(250.dp),
                onSuccess = {
                    val drawable = it.result.drawable
                    backgroundColor = extractColorFromDrawable(drawable)
                }
            )

        }
    }
}

@Composable
private fun DetailHeader(
    navController: NavController,
    viewModel: PokemonDetailViewModel,
    title: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(onClick = {
                viewModel.cleanPokemonId()
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    "Back Button"
                )
            }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}

private fun getTypeColor(type: String): Color {
    val typeColors = mapOf(
        "fire" to Color(0xFFF08030),
        "water" to Color(0xFF6890F0),
        "grass" to Color(0xFF78C850),
        "electric" to Color(0xFFF8D030),
        "bug" to Color(0xFFA8B820),
        "poison" to Color(0xFFA040A0),
        "normal" to Color(0xFFA8A878),
        "flying" to Color(0xFFA890F0),
        "ground" to Color(0xFFE0C068),
        "fairy" to Color(0xFFEE99AC),
        "fighting" to Color(0xFFC03028),
        "psychic" to Color(0xFFF85888),
        "rock" to Color(0xFFB8A038),
        "ghost" to Color(0xFF705898),
        "ice" to Color(0xFF98D8D8),
        "dragon" to Color(0xFF7038F8),
        "dark" to Color(0xFF705848),
        "steel" to Color(0xFFB8B8D0)
    )

    return typeColors[type.lowercase()] ?: Color.LightGray
}

private fun getTextColor(background: Color): Color {
    return if (background.luminance() < 0.5f) {
        Color.White
    } else {
        Color.Black
    }
}

private fun abbreviateStatName(name: String): String {

    return when (name) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SpA"
        "special-defense" -> "SpD"
        "speed" -> "SPD"
        else -> name
    }
}
