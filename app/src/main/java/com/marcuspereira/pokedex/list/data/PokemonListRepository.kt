package com.marcuspereira.pokedex.list.data

import com.marcuspereira.pokedex.common.model.Pokemon

interface PokemonListRepository {
    suspend fun getPokemonList(): Result<List<Pokemon>?>
}