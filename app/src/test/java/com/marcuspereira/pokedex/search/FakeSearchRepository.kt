package com.marcuspereira.pokedex.search

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.SearchPokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSearchRepository : SearchPokemonRepository {

    var shouldThrowException: Boolean = false
    var listPokemon: Result<List<Pokemon>> = Result.success(emptyList())

    var savedPokemon: Pokemon = Pokemon(
        id = 1,
        name = "name1",
        image = "image1"
    )

    var deletedPokemon: Pokemon = Pokemon(
        id = 1,
        name = "name1",
        image = "image1"
    )

    var observeList = MutableStateFlow<List<Pokemon>>(emptyList())

    var deletedAllList : List<Pokemon> = emptyList()

    override suspend fun getSearchPokemonList(): Result<List<Pokemon>?> {
        return listPokemon
    }

    override suspend fun savePokemonHistory(pokemon: Pokemon): Result<Unit> {
        if (shouldThrowException) {
            return Result.failure(Exception("Save error"))
        }

        savedPokemon = pokemon
        return Result.success(Unit)
    }

    override fun observeSearchLocalList(): Flow<List<Pokemon>> {
        return observeList
    }

    override suspend fun deletePokemonHistory(pokemon: Pokemon): Result<Unit> {
        if (shouldThrowException){
            return Result.failure(Exception("Error deleting Pokemon"))
        }

        deletedPokemon = pokemon
        return Result.success(Unit)
    }

    override suspend fun deleteAllHistory(pokemonList: List<Pokemon>): Result<Unit> {
        if (shouldThrowException){
            return Result.failure(Exception("Error deleting Pokemon List"))
        }

        deletedAllList = pokemonList
        return Result.success(Unit)
    }
}