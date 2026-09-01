package com.marcuspereira.pokedex.search

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.PokemonSearchRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

class SearchRepositoryTest {

    private val local = FakePokemonSearchLocalDataSource()
    private val remote = FakePokemonSearchRemoteDataSource()

    private val underTest by lazy {
        PokemonSearchRepository(
            local = local, remote = remote
        )
    }

    private val pokemon = Pokemon(
        id = 1,
        name = "name1",
        image = "image1"
    )

    @Test
    fun `Given result is success When get pokemon list Then return list`() {
        runTest {

            val list = listOf(
                Pokemon(
                    id = 1, name = "name1", image = "image1"
                )
            )

            remote.pokemonList = Result.success(list)

            val result = underTest.getSearchPokemonList()

            val expected = Result.success(list)

            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given remote failure When get pokemon list Then return remote error`() {
        runTest {

            val resultRemote = Result.failure<List<Pokemon>>(UnknownHostException())

            remote.pokemonList = resultRemote

            val result = underTest.getSearchPokemonList()

            val expected = resultRemote

            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given a pokemon When save pokemon history Then save pokemon successfully`() {
        runTest {

            local.shouldThrowException = false

            val result = underTest.savePokemonHistory(pokemon)

            assertEquals(pokemon, local.savedPokemon)
            assertEquals(Result.success(Unit), result)
        }
    }


    @Test
    fun `Given error When try save pokemon history Then return error`() {
        runTest {

            local.shouldThrowException = true

            val result = underTest.savePokemonHistory(pokemon)

            assertTrue(result.isFailure)

        }
    }

    @Test
    fun `Given local pokemon list When observe search local list Then retorn pokemon list`() {
        runTest {
            val pokemonList = listOf(
                pokemon,
                Pokemon(
                    id = 2,
                    name = "name2",
                    image = "image2"
                )
            )

            local.observeList.value = pokemonList

            val result = underTest.observeSearchLocalList().first()

            assertEquals(pokemonList, result)
        }
    }

    @Test
    fun `Given a pokemon When delete a pokemon history Then delete pokemon`() {
        runTest {

            local.shouldThrowException = false

            val result = underTest.deletePokemonHistory(pokemon)

            assertEquals(pokemon, local.deletedPokemon)
            assertEquals(Result.success(Unit), result)
        }
    }

    @Test
    fun `Given error When try delete pokemon Then return error`() {
        runTest {
            local.shouldThrowException = true

            val result = underTest.deletePokemonHistory(pokemon)

            assertTrue(result.isFailure)
        }
    }

    @Test
    fun `Given pokemon list When try delete all pokemon Then delete all pokemon successfully`() {
        runTest {
            val list = listOf(pokemon)

            local.shouldThrowException = false

            val result = underTest.deleteAllHistory(list)

            assertEquals(list, local.deleteAll)
            assertEquals(Result.success(Unit), result)
        }
    }

    @Test
    fun `Given error When try delete all pokemon Then return error`(){
        runTest {
            val list = listOf(pokemon)

            local.shouldThrowException = true

            val result = underTest.deleteAllHistory(list)

            assertTrue(result.isFailure)
        }
    }
}