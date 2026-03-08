package com.marcuspereira.pokedex.detail.presentation.ui

data class PokemonDetailUiState(
    val pokemon : PokemonDetailUiData? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Something went thing"
)

data class PokemonDetailUiData(
    val id: Int,
    val name: String,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val stats: List<PokemonStatUiData>
)

data class PokemonStatUiData(
    val name: String,
    val value: Int
)

