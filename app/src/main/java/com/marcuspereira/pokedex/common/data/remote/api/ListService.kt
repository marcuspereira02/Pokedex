package com.marcuspereira.pokedex.common.data.remote.api

import com.marcuspereira.pokedex.list.data.remote.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ListService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : Response<PokemonResponse>
}