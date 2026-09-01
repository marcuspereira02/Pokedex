package com.marcuspereira.pokedex.search

import app.cash.turbine.test
import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiData
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiState
import kotlinx.coroutines.test.runTest
import org.junit.Test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import java.net.UnknownHostException

class SearchViewModelTest {

    private val repository = FakeSearchRepository()

    private val underTest by lazy {
        PokemonSearchViewModel(repository, Dispatchers.Unconfined)
    }

    private val pokemonList = listOf(
        Pokemon(
            id = 1,
            name = "name1",
            image = "image1"
        )
    )

    val pokemon = Pokemon(
        id = 1,
        name = "name1",
        image = "image1"
    )

    @Test
    fun `Given pokemon list When search by name Then return matching pokemon`() {
        runTest {

            repository.listPokemon = Result.success(pokemonList)

            underTest.onQueryChanged("name1")

            underTest.uiPokemon.test {

                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = listOf(
                        PokemonSearchUiData(
                            id = 1,
                            image = "image1",
                            name = "name1"
                        )
                    ),
                    isError = false,
                    isOffline = false,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given pokemon list When search with blank query Then return empty state`() {
        runTest {

            underTest.onQueryChanged("")

            underTest.uiPokemon.test {

                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = emptyList(),
                    isError = false,
                    isOffline = false,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }

        }
    }

    @Test
    fun `Given no internet connection When search pokemon Then return message error`() {
        runTest {

            repository.listPokemon = Result.failure(UnknownHostException())

            underTest.uiPokemon.test {

                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = emptyList(),
                    isOffline = true,
                    isError = true,
                    errorMessage = "Not internet connection"
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given unknown error  When search pokemon Then return message error`() {
        runTest {

            repository.listPokemon = Result.failure(UnknownError())

            underTest.uiPokemon.test {

                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = emptyList(),
                    isOffline = false,
                    isError = true,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given a pokemon When saving history Then save pokemon in repository `() {
        runTest {

            val pokemonUiData = PokemonSearchUiData(
                id = pokemon.id, name = pokemon.name,
                image = pokemon.image
            )

            underTest.savePokemonHistory(pokemonUiData)

            assertEquals(pokemon, repository.savedPokemon)

        }
    }

    @Test
    fun `Given a list pokemon When observe list Then return list`() {
        runTest {
            val listPokemon = listOf<Pokemon>(
                Pokemon(
                    id = 1,
                    name = "name1",
                    image = "image1"
                ),
                Pokemon(
                    id = 2,
                    name = "name2",
                    image = "image2"
                )
            )

            val expected = listOf<PokemonSearchUiData>(
                PokemonSearchUiData(
                    id = 1,
                    name = "name1",
                    image = "image1"
                ),
                PokemonSearchUiData(
                    id = 2,
                    name = "name2",
                    image = "image2"
                )
            )

            repository.observeList.value = listPokemon

            val result = underTest.searchHistoryUiData()

            result.test {
                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given a pokemon When deleting pokemon Then delete pokemon in repository`() {
        runTest {

            val pokemonUiData = PokemonSearchUiData(
                id = pokemon.id, name = pokemon.name,
                image = pokemon.image
            )

            underTest.deletePokemon(pokemonUiData)

            assertEquals(pokemon, repository.deletedPokemon)
        }
    }

    @Test
    fun `Given a pokemon list When deleting a list Then delete pokemon list in repository`() {
        runTest {

            val list = listOf(
                PokemonSearchUiData(id = 1, name = "name1", image = "image1")
            )

            val expected = listOf(
                Pokemon(id = 1, name = "name1", image = "image1")
            )

            underTest.deleteAllPokemon(list)

            assertEquals(expected , repository.deletedAllList)

        }
    }
}