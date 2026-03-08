package com.marcuspereira.pokedex

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListScreen

@Composable
fun PokedexApp(listViewModel: PokemonListViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "pokemonListScreen"){
        composable(route = "pokemonListScreen"){
            PokemonListScreen(listViewModel)
        }
    }
}