package com.marcuspereira.pokedex.search.data

import com.marcuspereira.pokedex.common.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface SearchPokemonRepository {
    suspend fun getSearchPokemonList(): Result<List<Pokemon>?>

    suspend fun savePokemonHistory(pokemon: Pokemon): Result<Unit>

    fun observeSearchLocalList() : Flow<List<Pokemon>>

    suspend fun deletePokemonHistory(pokemon: Pokemon) : Result<Unit>

    suspend fun deleteAllHistory(pokemonList: List<Pokemon>) : Result<Unit>
}