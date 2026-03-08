package com.marcuspereira.pokedex.list.data.remote

import android.accounts.NetworkErrorException

import com.marcuspereira.pokedex.common.model.Pokemon

class PokemonListRemoteDataSource(
    private val listService: ListService
) {

    suspend fun getPokemonList(): Result<List<Pokemon>?> {
        return try {
            val response = listService.getPokemonList(limit = 30, offset = 0)
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