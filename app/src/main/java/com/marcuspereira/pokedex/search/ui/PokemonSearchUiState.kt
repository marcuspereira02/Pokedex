package com.marcuspereira.pokedex.search.ui

data class PokemonSearchUiState(
    val data : PokemonSearchUiData? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Something went thing"
)

data class PokemonSearchUiData(
    val id: Int,
    val image: String,
    val name: String,
    val types: List<String>
)
