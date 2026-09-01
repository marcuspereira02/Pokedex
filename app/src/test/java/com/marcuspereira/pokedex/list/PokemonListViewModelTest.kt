package com.marcuspereira.pokedex.list

import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListUiData
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListUiState
import java.net.UnknownHostException

class PokemonListViewModelTest {

    private val repository = FakeListRepository()

    private val underTest by lazy {
        PokemonListViewModel(repository)
    }

    @Test
    fun `Given fresh viewModel When collecting pokemon list Then assert expected value`() {
        runTest {

            val list = listOf(
                Pokemon(
                    id = 1,
                    name = "name1",
                    image = "image1"
                )
            )

            repository.listPokemon = Result.success(list)

            underTest.uiAllPokemon.test {

                val expected = PokemonListUiState(
                    list = listOf(
                        PokemonListUiData(
                            id = 1,
                            name = "name1",
                            image = "image1"
                        )
                    ),
                    isLoading = false,
                    isError = false,
                    errorMessage = "Something went wrong"
                )

                assertEquals(expected, awaitItem())
            }

        }
    }

    @Test
    fun `Given no internet When get pokemon list Then emits network error`() {
        runTest {

            repository.listPokemon = Result.failure(UnknownHostException())

            underTest.uiAllPokemon.test {
                val expected = PokemonListUiState(
                    isLoading = false,
                    list = emptyList(),
                    isError = true,
                    errorMessage = "Not internet connection"
                )
                assertEquals(expected, awaitItem())
            }
        }
    }

    @Test
    fun `Given unknown exception When get pokemon list Then emits unknown error`() {
        runTest {
            repository.listPokemon = Result.failure(UnknownError())

            underTest.uiAllPokemon.test {
                val expected = PokemonListUiState(
                    isLoading = false,
                    list = emptyList(),
                    isError = true,
                    errorMessage = "Something went wrong"
                )
                assertEquals(expected, awaitItem())
            }
        }
    }
}