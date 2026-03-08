package com.marcuspereira.pokedex.list.presentation.ui

data class PokemonListUiState(
    val list : List<PokemonListUiData> = emptyList(),
    val isLoading : Boolean = false,
    val isError : Boolean = false,
    val errorMessage: String = "Something went thing"
)

data class PokemonListUiData(
    val id : Int,
    val name: String,
    val image: String
)