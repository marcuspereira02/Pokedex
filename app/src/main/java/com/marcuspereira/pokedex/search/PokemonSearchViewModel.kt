package com.marcuspereira.pokedex.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.marcuspereira.pokedex.common.data.remote.api.RetrofitClient
import com.marcuspereira.pokedex.common.data.remote.api.ListService
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiData
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PokemonSearchViewModel(
    private val service: ListService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiPokemon = MutableStateFlow(PokemonSearchUiState())
    val uiPokemon: StateFlow<PokemonSearchUiState> = _uiPokemon.asStateFlow()

    private var allPokemon: List<PokemonSearchUiData> = emptyList()

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch(dispatcher) {
            try {
                _uiPokemon.value = PokemonSearchUiState(isLoading = true)

                val response = service.getPokemonList(2000, 0)
                if (response.isSuccessful) {
                    allPokemon = response.body()?.results?.map {
                        PokemonSearchUiData(
                            id = it.id,
                            name = it.name,
                            image = it.imageUrl
                        )
                    }
                        .orEmpty()
                    _uiPokemon.value = PokemonSearchUiState()

                } else if (response.code() == 404) {

                    _uiPokemon.value = PokemonSearchUiState(
                        isError = true,
                        errorMessage = "No Pokémon found. Please try again."
                    )

                } else {
                    _uiPokemon.value = PokemonSearchUiState(isError = true)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                if (ex is UnknownHostException) {
                    _uiPokemon.value = PokemonSearchUiState(
                        isError = true,
                        isOffline = true,
                        errorMessage = "Not internet connection"
                    )

                } else {
                    _uiPokemon.value = PokemonSearchUiState(
                        isError = true
                    )
                }
            }
        }
    }

    fun onQueryChanged(query: String) {

        if(_uiPokemon.value.isOffline){
            return
        }

        val formattedQuery = query
            .trim()
            .lowercase()

        if (formattedQuery.isBlank()) {
            _uiPokemon.value = PokemonSearchUiState()
            return
        }

        val filteredPokemon = allPokemon
            .filter { pokemon ->
                pokemon.name.contains(
                    other = formattedQuery,
                    ignoreCase = true
                )
            }
            .take(30)

        _uiPokemon.value = PokemonSearchUiState(
            data = filteredPokemon
        )

    }

    fun retryLoadPokemonList(){
        loadPokemonList()
    }

    companion object {

        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val service = RetrofitClient.retrofitInstance.create(ListService::class.java)
                return PokemonSearchViewModel(
                    service
                ) as T
            }
        }
    }

}