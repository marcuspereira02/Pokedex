package com.marcuspereira.pokedex.list.data.remote

import com.marcuspereira.pokedex.common.model.Pokemon

interface RemoteListDataSource {
    suspend fun getPokemonList(): Result<List<Pokemon>?>
}