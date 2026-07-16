package com.marcuspereira.pokedex.list.data

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.data.local.LocalListDataSource
import com.marcuspereira.pokedex.list.data.remote.RemoteListDataSource

class ListRepository(
    private val local: LocalListDataSource,
    private val remote: RemoteListDataSource
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