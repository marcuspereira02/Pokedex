package com.marcuspereira.pokedex.detail

import com.marcuspereira.pokedex.detail.data.DetailService
import com.marcuspereira.pokedex.common.data.remote.dto.PokemonDetailDto
import retrofit2.Response

class FakeDetailService : DetailService {
    var pokemonByNameResponse: Response<PokemonDetailDto>? = null
    var pokemonByNameException: Exception? = null

    override suspend fun getDetailPokemon(pokemonName: String): Response<PokemonDetailDto> {
        pokemonByNameException?.let { throw it }

        return pokemonByNameResponse ?: throw IllegalStateException("Response must be set in test")
    }
}