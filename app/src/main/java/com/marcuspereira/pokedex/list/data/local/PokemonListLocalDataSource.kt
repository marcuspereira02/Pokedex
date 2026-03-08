package com.marcuspereira.pokedex.list.data.local

import com.marcuspereira.pokedex.common.data.local.PokemonDao
import com.marcuspereira.pokedex.common.data.local.PokemonEntity
import com.marcuspereira.pokedex.common.model.Pokemon

class PokemonListLocalDataSource(private val dao : PokemonDao){

    suspend fun getPokemonList() : List<Pokemon>{

        val entities = dao.getPokemonList()

        return entities.map { pokemonEntities ->
           Pokemon(
               id = pokemonEntities.id,
               name = pokemonEntities.name,
               image = pokemonEntities.image
           )
        }
    }

    suspend fun updateLocalItems(pokemon: List<Pokemon>){

        val entities = pokemon.map { pokemon ->
            PokemonEntity(
                id = pokemon.id,
                name = pokemon.name,
                image = pokemon.image
            )
        }

        dao.insertPokemonList(entities)
    }

}