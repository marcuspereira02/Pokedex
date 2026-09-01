package com.marcuspereira.pokedex.search

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.remote.SearchRemoteDataSource

class FakePokemonSearchRemoteDataSource : SearchRemoteDataSource {

    var pokemonList : Result<List<Pokemon>> = Result.success(emptyList() )

    override suspend fun getPokemonSearchList(): Result<List<Pokemon>?> {
        return pokemonList
    }
}