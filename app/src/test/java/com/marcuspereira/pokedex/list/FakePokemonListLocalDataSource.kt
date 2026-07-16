package com.marcuspereira.pokedex.list

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.data.local.LocalListDataSource

class FakePokemonListLocalDataSource : LocalListDataSource {

    var pokemon : List<Pokemon> = emptyList()
    var update : List<Pokemon> = emptyList()

    override suspend fun getPokemonList(): List<Pokemon> {
        return pokemon
    }

    override suspend fun updateLocalItems(pokemon: List<Pokemon>) {
        update = pokemon
    }
}