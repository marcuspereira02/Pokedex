package com.marcuspereira.pokedex.detail

import app.cash.turbine.test
import com.marcuspereira.pokedex.common.data.remote.dto.PokemonDetailDto
import com.marcuspereira.pokedex.detail.ui.PokemonDetailUiData
import com.marcuspereira.pokedex.detail.ui.PokemonDetailUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import org.junit.Test
import java.net.UnknownHostException


class DetailViewModelTest {
    private val service = FakeDetailService()
    private val underTest by lazy {
        PokemonDetailViewModel(service, Dispatchers.Unconfined)
    }

    @Test
    fun `Given response is success When get pokemon detail Then return pokemon ui data`() {
        runTest {
            val pokemon = PokemonDetailDto(
                id = 1,
                name = "name1",
                types = emptyList(),
                weight = 1,
                height = 1,
                stats = emptyList()
            )

            service.pokemonByNameResponse = Response.success(pokemon)

            underTest.fetchPokemonDetail("name1")

            underTest.uiPokemon.test {
                val expected = PokemonDetailUiState(
                    isLoading = false,
                    data = PokemonDetailUiData(
                        id = 1,
                        name = "name1",
                        image = pokemon.imageUrl,
                        types = emptyList(),
                        weight = 1,
                        height = 1,
                        stats = emptyList()
                    ),
                    isError = false,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given pokemon is null When get pokemon detail Then return message error`(){
        runTest {
            service.pokemonByNameResponse = Response.error(
                404,
                "Not Found".toResponseBody("application/json".toMediaType())
            )

            underTest.fetchPokemonDetail("1")

            underTest.uiPokemon.test {
                val expected = PokemonDetailUiState(
                    isLoading = false,
                    data = null,
                    isError = true,
                    errorMessage = "Something went wrong"
                )
                assertEquals(expected,awaitItem())
            }

        }
    }

    @Test
    fun `Given no internet When get pokemon detail Then return error message`() {
        runTest {
            service.pokemonByNameException = UnknownHostException()

            underTest.fetchPokemonDetail("1")
            underTest.uiPokemon.test {

                val expected = PokemonDetailUiState(
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
    fun `Given unknown error When get pokemon detail Then return error message`() {
        runTest {
            service.pokemonByNameException = RuntimeException("error")

            underTest.fetchPokemonDetail("1")
            underTest.uiPokemon.test {

                val expected = PokemonDetailUiState(
                    isError = true,
                    errorMessage = "Something went wrong"
                )
                assertEquals(expected, awaitItem())
            }
        }
    }
}