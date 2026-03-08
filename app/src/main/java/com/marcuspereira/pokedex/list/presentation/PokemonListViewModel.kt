package com.marcuspereira.pokedex.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.marcuspereira.pokedex.PokedexApplication
import com.marcuspereira.pokedex.list.data.ListRepository
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListUiData
import com.marcuspereira.pokedex.list.presentation.ui.PokemonListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class   PokemonListViewModel(
    private val repository: ListRepository
) : ViewModel() {
    private val _uiAllPokemon = MutableStateFlow(PokemonListUiState())
    val uiAllPokemon: StateFlow<PokemonListUiState> = _uiAllPokemon

    init {
        fetchAllPokemon()
    }

    private fun fetchAllPokemon() {
        _uiAllPokemon.value = PokemonListUiState(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPokemonList()
            if (result.isSuccess) {
                val pokemon = result.getOrNull()
                if (pokemon != null) {
                    val pokemonUiData = pokemon.map {
                        PokemonListUiData(
                            id = it.id,
                            name = it.name,
                            image = it.image
                        )
                    }
                    _uiAllPokemon.value = PokemonListUiState(list = pokemonUiData)
                }
            } else {
                val ex = result.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiAllPokemon.value = PokemonListUiState(
                        isError = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiAllPokemon.value = PokemonListUiState(
                        isError = true
                    )
                }
            }
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return PokemonListViewModel(
                    repository = (application as PokedexApplication).repository
                ) as T
            }
        }
    }

}