package com.marcuspereira.pokedex.list

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.data.ListRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import junit.framework.TestCase.assertEquals
import java.net.UnknownHostException

class PokemonListRepositoryTest {

    private val local = FakePokemonListLocalDataSource()
    private val remote = FakePokemonListRemoteDataSource()

    private val underTest by lazy {
        ListRepository(local = local, remote = remote)
    }

    @Test
    fun `Given result is success When get all pokemon's Then update local items`() {

        runTest {
            val list = listOf(
                Pokemon(
                    id = 1,
                    name = "pokemon1",
                    image = "image1"
                )
            )

            remote.pokemonList = Result.success(list)
            local.pokemon = list

            val result = underTest.getPokemonList()

            val expected = Result.success(list)

            assertEquals(expected, result)
            assertEquals(local.update, list)
        }

    }

    @Test
    fun `Given remote failure and no local data When get all pokemon's Then return remote error`() {
        runTest {

            val resultRemote = Result.failure<List<Pokemon>>(UnknownHostException())

            remote.pokemonList = resultRemote
            local.pokemon = emptyList()

            val result = underTest.getPokemonList()

            val expected = resultRemote

            assertEquals(expected, result)
        }

    }

    @Test
    fun `Given no internet connection When get all pokemon's Then return local data`() {
        runTest {

            val list = listOf(
                Pokemon(
                    id = 1,
                    name = "name1",
                    image = "image1"
                )
            )

            remote.pokemonList = Result.failure(UnknownHostException())
            local.pokemon = list

            val result = underTest.getPokemonList()

            val expected = Result.success(list)

            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given unknown error When get all pokemon's Then return remote result`() {
        runTest {
            val remoteResult = Result.failure<List<Pokemon>>(UnknownError())

            remote.pokemonList = remoteResult
            local.pokemon = emptyList()

            val result = underTest.getPokemonList()

            val expected = remoteResult

            assertEquals(expected, result)

        }
    }
}