package com.marcuspereira.pokedex.search.data

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.local.LocalSearchDataSource
import com.marcuspereira.pokedex.search.data.remote.SearchRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PokemonSearchRepository(
    private val local: LocalSearchDataSource,
    private val remote: SearchRemoteDataSource
) : SearchPokemonRepository {

    override suspend fun getSearchPokemonList(): Result<List<Pokemon>?> {
        return try {
            val result = remote.getPokemonSearchList()
            if (result.isSuccess) {
                val pokemonRemote = result.getOrNull() ?: emptyList()
                Result.success(pokemonRemote)
            } else {
                result
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override suspend fun savePokemonHistory(pokemon: Pokemon): Result<Unit> {

        return try {
            local.savePokemonHistory(pokemon)
            Result.success(Unit)

        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override fun observeSearchLocalList(): Flow<List<Pokemon>> {
        return local.observeSearchLocalList()
    }

    override suspend fun deletePokemonHistory(pokemon: Pokemon): Result<Unit> {
        return try {
            local.deleteSearchPokemon(pokemon)
            Result.success(Unit)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

    override suspend fun deleteAllHistory(pokemonList: List<Pokemon>): Result<Unit> {
        return try {
            local.deleteSearchListPokemon(pokemonList)
            Result.success(Unit)

        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

}