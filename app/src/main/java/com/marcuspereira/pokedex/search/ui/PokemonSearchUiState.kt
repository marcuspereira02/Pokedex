package com.marcuspereira.pokedex.search.ui

data class PokemonSearchUiState(
    val data :List <PokemonSearchUiData> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isOffline : Boolean = false,
    val errorMessage: String = "Something went wrong"
)

data class PokemonSearchUiData(
    val id: Int,
    val image: String,
    val name: String
)
