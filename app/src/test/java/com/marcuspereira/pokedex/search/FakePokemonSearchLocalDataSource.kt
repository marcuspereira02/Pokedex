package com.marcuspereira.pokedex.search

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.local.LocalSearchDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePokemonSearchLocalDataSource : LocalSearchDataSource {

    var observeList = MutableStateFlow<List<Pokemon>>(emptyList())
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

    var shouldThrowException: Boolean = false

    var deleteAll : List<Pokemon> = emptyList()

    override fun observeSearchLocalList(): Flow<List<Pokemon>> {
       return observeList
    }

    override suspend fun savePokemonHistory(pokemon: Pokemon) {
       if (shouldThrowException) {
           throw Exception("Save error")
       }
        savedPokemon = pokemon
    }

    override suspend fun deleteSearchPokemon(pokemon: Pokemon) {
        if (shouldThrowException) {
            throw Exception("Error deleting Pokemon")
        }
        deletedPokemon = pokemon
    }

    override suspend fun deleteSearchListPokemon(pokemon: List<Pokemon>) {
        if (shouldThrowException) {
            throw Exception("Error deleting Pokemon list")
        }

        deleteAll = pokemon
    }
}