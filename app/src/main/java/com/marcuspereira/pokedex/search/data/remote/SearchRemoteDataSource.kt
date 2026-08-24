package com.marcuspereira.pokedex.search.data.remote

import com.marcuspereira.pokedex.common.model.Pokemon

interface SearchRemoteDataSource {

    suspend fun getPokemonSearchList(): Result<List<Pokemon>?>
}