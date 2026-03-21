package com.marcuspereira.pokedex.detail.data

import com.google.gson.annotations.SerializedName


data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val types: List<TypeSlotDto>,
    val weight: Int,
    val height: Int,
    val stats: List<StatsSlotDto>
){
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

data class TypeSlotDto(
    val slot: Int,
    val type: TypeNameDto
)
data class TypeNameDto(
    val name :  String
)

data class StatsSlotDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatNameDto
)

data class StatNameDto(
    val name : String,
    val url: String
)