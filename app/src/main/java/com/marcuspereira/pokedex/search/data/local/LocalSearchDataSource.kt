package com.marcuspereira.pokedex.search.data.local

import com.marcuspereira.pokedex.common.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface LocalSearchDataSource {
    fun observeSearchLocalList(): Flow<List<Pokemon>>

    suspend fun savePokemonHistory(pokemon: Pokemon)

    suspend fun deleteSearchPokemon(pokemon: Pokemon)

    suspend fun deleteSearchListPokemon(pokemon: List<Pokemon>)

}