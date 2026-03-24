package com.marcuspereira.pokedex

import android.app.Application
import androidx.room.Room
import com.marcuspereira.pokedex.common.data.local.PokedexDataBase
import com.marcuspereira.pokedex.common.data.remote.api.RetrofitClient
import com.marcuspereira.pokedex.list.data.ListRepository
import com.marcuspereira.pokedex.list.data.local.PokemonListLocalDataSource
import com.marcuspereira.pokedex.list.data.remote.ListService
import com.marcuspereira.pokedex.list.data.remote.PokemonListRemoteDataSource

class PokedexApplication : Application() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PokedexDataBase::class.java, name = "database-pokedex"
        ).build()
    }

    private val listService by lazy {
        RetrofitClient.retrofitInstance.create(ListService::class.java)
    }

    private val local by lazy {
        PokemonListLocalDataSource(
            db.getPokemonDao()
        )
    }

    private val remote by lazy {
        PokemonListRemoteDataSource(
            listService
        )
    }

    val repository by lazy {
        ListRepository(
            local = local,
            remote = remote
        )
    }

}