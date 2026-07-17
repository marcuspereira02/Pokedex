package com.marcuspereira.pokedex.list

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.data.PokemonListRepository

class FakeListRepository : PokemonListRepository {

    var listPokemon : Result<List<Pokemon>> = Result.success(emptyList())

    override suspend fun getPokemonList(): Result<List<Pokemon>?> {
        return listPokemon
    }
}