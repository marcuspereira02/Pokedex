package com.marcuspereira.pokedex.list.data.local
import com.marcuspereira.pokedex.common.model.Pokemon

interface LocalListDataSource {

    suspend fun getPokemonList() : List<Pokemon>

    suspend fun updateLocalItems (pokemon: List<Pokemon>)
}