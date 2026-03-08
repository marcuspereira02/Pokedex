package com.marcuspereira.pokedex.list.data

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.data.local.PokemonListLocalDataSource
import com.marcuspereira.pokedex.list.data.remote.PokemonListRemoteDataSource

class ListRepository(
    private val local: PokemonListLocalDataSource,
    private val remote: PokemonListRemoteDataSource
) {

    suspend fun getPokemonList(): Result<List<Pokemon>?> {
        return try {
            val result = remote.getPokemonList()
            if (result.isSuccess) {
                val pokemonRemote = result.getOrNull() ?: emptyList()
                if (pokemonRemote.isNotEmpty()) {
                    local.updateLocalItems(pokemonRemote)
                }
                Result.success(local.getPokemonList())
            } else {
                val localData = local.getPokemonList()
                if (localData.isEmpty()) {
                    return result
                } else {
                    Result.success(localData)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

}