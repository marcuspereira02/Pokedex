package com.marcuspereira.pokedex.search

import com.marcuspereira.pokedex.detail.data.DetailService
import com.marcuspereira.pokedex.common.data.remote.dto.PokemonDetailDto
import retrofit2.Response

class FakeSearchService : DetailService {
    var pokemon: Response<PokemonDetailDto>? = null
    var exception: Exception? = null

    override suspend fun getDetailPokemon(pokemonName: String): Response<PokemonDetailDto> {
        exception?.let { throw it }

        return pokemon ?: throw IllegalStateException("Response must be set in test")
    }
}