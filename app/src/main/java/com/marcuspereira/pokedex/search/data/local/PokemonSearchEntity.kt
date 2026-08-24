package com.marcuspereira.pokedex.search.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_pokemon")
data class RecentPokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
    val searchedAt: Long
)