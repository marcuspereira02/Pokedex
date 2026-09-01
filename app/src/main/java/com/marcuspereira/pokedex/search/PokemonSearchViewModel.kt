package com.marcuspereira.pokedex.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.marcuspereira.pokedex.PokedexApplication
import com.marcuspereira.pokedex.common.model.Pokemon
import com.marcuspereira.pokedex.search.data.PokemonSearchRepository
import com.marcuspereira.pokedex.search.data.SearchPokemonRepository
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiData
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PokemonSearchViewModel(
    private val repository: SearchPokemonRepository,
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
            _uiPokemon.value = PokemonSearchUiState(isLoading = true)

            val result = repository.getSearchPokemonList()
            if (result.isSuccess) {
                allPokemon = result.getOrNull()?.map {
                    PokemonSearchUiData(
                        id = it.id,
                        name = it.name,
                        image = it.image
                    )
                }
                    .orEmpty()
                _uiPokemon.value = PokemonSearchUiState()

            } else {
                val ex = result.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiPokemon.value = PokemonSearchUiState(
                        isError = true,
                        isOffline = true,
                        errorMessage = "Not internet connection"
                    )
                } else {
                    _uiPokemon.value = PokemonSearchUiState(isError = true)
                }
            }
        }
    }

    fun onQueryChanged(query: String) {

        if (_uiPokemon.value.isOffline) {
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

    fun retryLoadPokemonList() {
        loadPokemonList()
    }

    fun savePokemonHistory(pokemonSearchUiData: PokemonSearchUiData) {
        viewModelScope.launch(dispatcher){

            val pokemon = Pokemon(
                id = pokemonSearchUiData.id,
                name = pokemonSearchUiData.name,
                image = pokemonSearchUiData.image
            )

            repository.savePokemonHistory(pokemon)
        }
    }

    fun searchHistoryUiData(): Flow<List<PokemonSearchUiData>> {

        return repository.observeSearchLocalList().map { pokemonList ->
            pokemonList.map {
                PokemonSearchUiData(
                    id = it.id,
                    name = it.name,
                    image = it.image
                )
            }
        }
    }

    fun deletePokemon(pokemonSearchUiData: PokemonSearchUiData) {
        viewModelScope.launch(dispatcher) {

            val pokemon = Pokemon(
                id = pokemonSearchUiData.id,
                name = pokemonSearchUiData.name,
                image = pokemonSearchUiData.image
            )

            repository.deletePokemonHistory(pokemon)
        }
    }

    fun deleteAllPokemon(pokemonUiDataList: List<PokemonSearchUiData>) {
        viewModelScope.launch(dispatcher) {

            val pokemonList = pokemonUiDataList.map {
                Pokemon(
                    id = it.id,
                    name = it.name,
                    image = it.image
                )
            }

            repository.deleteAllHistory(pokemonList)
        }
    }

    companion object {

        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return PokemonSearchViewModel(
                    repository = (application as PokedexApplication).searchRepository
                ) as T
            }
        }
    }

}