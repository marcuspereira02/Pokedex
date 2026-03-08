package com.marcuspereira.pokedex.common.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemonentity")
    suspend fun getPokemonList(): List<PokemonEntity>

}