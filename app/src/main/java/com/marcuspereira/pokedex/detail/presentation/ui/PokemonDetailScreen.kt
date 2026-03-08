package com.marcuspereira.pokedex.detail.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.marcuspereira.pokedex.detail.presentation.PokemonDetailViewModel
import com.marcuspereira.pokedex.ui.theme.PokedexTheme

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

        pokemon.pokemon != null -> {

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                DetailHeader(
                    navController = navController,
                    viewModel = viewModel,
                    title = pokemon.pokemon!!.name
                )
                PokemonDetailContent()
            }
        }
    }


}

@Composable
private fun PokemonDetailContent(modifier: Modifier = Modifier) {



}

@Composable
private fun DetailHeader(
    navController: NavController,
    viewModel: PokemonDetailViewModel,
    title: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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

@Preview(showBackground = true)
@Composable
private fun PokemonDetailPreview(modifier: Modifier = Modifier) {

    PokedexTheme() {


    }

}