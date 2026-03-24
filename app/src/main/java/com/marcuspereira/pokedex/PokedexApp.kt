package com.marcuspereira.pokedex

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.marcuspereira.pokedex.detail.PokemonDetailViewModel
import com.marcuspereira.pokedex.detail.ui.PokemonDetailScreen
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListScreen
import com.marcuspereira.pokedex.search.PokemonSearchViewModel
import com.marcuspereira.pokedex.search.ui.PokemonSearchScreen

@Composable
fun PokedexApp(
    listViewModel: PokemonListViewModel,
    detailViewModel: PokemonDetailViewModel,
    searchViewModel: PokemonSearchViewModel
    ) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "pokemonListScreen") {
        composable(route = "pokemonListScreen") {
            PokemonListScreen(
                viewModel = listViewModel,
                navController = navController
            )
        }
        composable(
            route = "pokemonDetail" + "/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val pokemonId = requireNotNull(backStackEntry.arguments?.getString("itemId"))
            PokemonDetailScreen(
                id = pokemonId,
                viewModel = detailViewModel,
                navController = navController
            )
        }
        composable(
            route = "searchScreen" + "/{query}",
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val querySearch = requireNotNull(backStackEntry.arguments?.getString("query"))
            PokemonSearchScreen(querySearch, navController, searchViewModel)
        }
    }
}