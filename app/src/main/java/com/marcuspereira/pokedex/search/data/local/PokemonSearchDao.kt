package com.marcuspereira.pokedex.search.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonHistory(pokemon: RecentPokemonEntity)

    @Query("DELETE FROM recent_pokemon WHERE id = :id")
    suspend fun deletePokemon(id: Int)

    @Query("DELETE FROM recent_pokemon")
    suspend fun deleteAllPokemon()

    @Query("""SELECT * FROM recent_pokemon ORDER BY searchedAt DESC""")
    fun observePokemonHistory(): Flow<List<RecentPokemonEntity>>

}