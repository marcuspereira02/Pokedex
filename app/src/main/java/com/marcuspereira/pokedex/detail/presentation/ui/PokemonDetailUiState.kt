package com.marcuspereira.pokedex.detail.presentation.ui

data class PokemonDetailUiState(
    val data : PokemonDetailUiData? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Something went thing"
)

data class PokemonDetailUiData(
    val id: Int,
    val image: String,
    val name: String,
    val types: List<String>,
    val weight: Int,
    val height: Int,
    val stats: List<PokemonStatsUiData>
)

data class PokemonStatsUiData(
    val name: String,
    val value: Int
)

