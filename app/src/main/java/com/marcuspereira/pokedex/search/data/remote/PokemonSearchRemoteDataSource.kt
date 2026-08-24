package com.marcuspereira.pokedex.search.data.remote

import android.accounts.NetworkErrorException
import com.marcuspereira.pokedex.common.data.remote.api.ListService
import com.marcuspereira.pokedex.common.model.Pokemon

class PokemonSearchRemoteDataSource(
    private val listService: ListService
) : SearchRemoteDataSource {
    override suspend fun getPokemonSearchList(): Result<List<Pokemon>?> {
        return try {
            val response = listService.getPokemonList(limit = 2000, 0)
            if (response.isSuccessful) {
                val pokemon = response.body()?.results?.map {
                    Pokemon(
                        id = it.id,
                        name = it.name,
                        image = it.imageUrl
                    )
                }
                Result.success(pokemon)
            } else {
                Result.failure(NetworkErrorException(response.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}