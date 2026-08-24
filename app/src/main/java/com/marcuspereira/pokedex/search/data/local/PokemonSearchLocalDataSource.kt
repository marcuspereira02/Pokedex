package com.marcuspereira.pokedex.search.data.local

import com.marcuspereira.pokedex.common.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonSearchLocalDataSource(
    private val dao: PokemonSearchDao
) : LocalSearchDataSource {
    override fun observeSearchLocalList(): Flow<List<Pokemon>> {
        return dao.observePokemonHistory().map { entities ->
            entities.map { entity ->
                Pokemon(
                    id = entity.id,
                    name = entity.name,
                    image = entity.image
                )
            }
        }
    }

    override suspend fun savePokemonHistory(pokemon: Pokemon) {
        val entity =
            RecentPokemonEntity(
                id = pokemon.id,
                name = pokemon.name,
                image = pokemon.image,
                searchedAt = System.currentTimeMillis()
            )
        dao.insertPokemonHistory(entity)
    }

    override suspend fun deleteSearchPokemon(pokemon: Pokemon) {
        dao.deletePokemon(pokemon.id)
    }

    override suspend fun deleteSearchListPokemon(pokemon: List<Pokemon>) {
        dao.deleteAllPokemon()
    }
}
