package com.marcuspereira.pokedex.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcuspereira.pokedex.search.data.local.PokemonSearchDao
import com.marcuspereira.pokedex.search.data.local.RecentPokemonEntity

@Database([PokemonEntity::class, RecentPokemonEntity::class], version = 1)
abstract class PokedexDataBase : RoomDatabase() {
    abstract fun getPokemonDao(): PokemonDao
    abstract fun getPokemonSearchDao(): PokemonSearchDao
}