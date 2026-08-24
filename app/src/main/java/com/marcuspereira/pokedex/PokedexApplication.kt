package com.marcuspereira.pokedex

import android.app.Application
import androidx.room.Room
import com.marcuspereira.pokedex.common.data.local.PokedexDataBase
import com.marcuspereira.pokedex.common.data.remote.api.RetrofitClient
import com.marcuspereira.pokedex.list.data.ListRepository
import com.marcuspereira.pokedex.list.data.local.PokemonListLocalDataSource
import com.marcuspereira.pokedex.common.data.remote.api.ListService
import com.marcuspereira.pokedex.list.data.remote.PokemonListRemoteDataSource
import com.marcuspereira.pokedex.search.data.PokemonSearchRepository
import com.marcuspereira.pokedex.search.data.local.PokemonSearchLocalDataSource
import com.marcuspereira.pokedex.search.data.remote.PokemonSearchRemoteDataSource

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

    private val listLocal by lazy {
        PokemonListLocalDataSource(
            db.getPokemonDao()
        )
    }

    private val listRemote by lazy {
        PokemonListRemoteDataSource(
            listService
        )
    }

    val listRepository by lazy {
        ListRepository(
            local = listLocal,
            remote = listRemote
        )
    }

    private val searchLocal by lazy {
        PokemonSearchLocalDataSource(db.getPokemonSearchDao())
    }

    private val searchRemote by lazy {
        PokemonSearchRemoteDataSource(listService)
    }

    val searchRepository by lazy {
        PokemonSearchRepository(
            local = searchLocal,
            remote = searchRemote
        )
    }

}