package com.marcuspereira.pokedex.search

import app.cash.turbine.test
import com.marcuspereira.pokedex.common.data.remote.dto.PokemonDetailDto
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiData
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiState
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.UnknownHostException
import kotlin.math.exp

class SearchViewModelTest {

    private val service = FakeSearchService()

    private val underTest by lazy {
        PokemonSearchViewModel(service, Dispatchers.Unconfined)
    }

    @Test
    fun `Given response is success When search Then return pokemon`() {
        runTest {
            val pokemon =
                PokemonDetailDto(
                    id = 1,
                    name = "name1",
                    types = emptyList(),
                    weight = 1,
                    height = 1,
                    stats = emptyList()

                )

            service.pokemon = Response.success(pokemon)

            val name = "name1"
            underTest.fetchPokemon(name)

            underTest.uiPokemon.test {
                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = PokemonSearchUiData(
                        id = 1,
                        image = pokemon.imageUrl,
                        name = "name1"
                    ),
                    isError = false,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }


        }
    }

    @Test
    fun `Given pokemon is null When search pokemon Then return message error`() {
        runTest {
            service.pokemon = Response.error(
                404,
                "Not Found".toResponseBody("application/json".toMediaType())
            )

            underTest.fetchPokemon("name1")

            underTest.uiPokemon.test {
                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = null,
                    isError = true,
                    errorMessage = "No Pokémon found. Please try again."
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given not internet connection When search pokemon Then return message error`() {
        runTest {
            service.exception = UnknownHostException()

            underTest.fetchPokemon("name1")

            underTest.uiPokemon.test {

                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = null,
                    isError = true,
                    errorMessage = "Not internet connection"
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given unknown error When get pokemon Then return error message`() {
        runTest {
            service.exception = RuntimeException("error")

            underTest.fetchPokemon("name1")
            underTest.uiPokemon.test {
                val expected = PokemonSearchUiState(
                    isLoading = false,
                    data = null,
                    isError = true,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }
        }

    }
}