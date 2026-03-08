package com.marcuspereira.pokedex.detail.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {

    @GET("pokemon/{pokemonName}")
    suspend fun getDetailPokemon(@Path("pokemonName") pokemonName: String): Response<PokemonDetailDto>
}