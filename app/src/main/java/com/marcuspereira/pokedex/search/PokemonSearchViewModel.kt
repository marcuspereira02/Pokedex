package com.marcuspereira.pokedex.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.marcuspereira.pokedex.common.data.remote.api.DetailService
import com.marcuspereira.pokedex.common.data.remote.api.RetrofitClient
import com.marcuspereira.pokedex.common.data.remote.dto.PokemonDetailDto
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiData
import com.marcuspereira.pokedex.search.ui.PokemonSearchUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PokemonSearchViewModel(
    private val service: DetailService
) : ViewModel() {

    private val _uiPokemon = MutableStateFlow(PokemonSearchUiState())
    val uiPokemon: StateFlow<PokemonSearchUiState> = _uiPokemon

    fun fetchPokemon(
        id: String
    ) {
        try {
            _uiPokemon.value = PokemonSearchUiState(isLoading = true)
            viewModelScope.launch(Dispatchers.IO) {
                val response = service.getDetailPokemon(id)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val pokemonUiData = converterPokemonDto(data)
                        _uiPokemon.value = PokemonSearchUiState(data = pokemonUiData)
                    }
                } else {
                    _uiPokemon.value = PokemonSearchUiState(isError = true)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (ex is UnknownHostException) {
                _uiPokemon.value = PokemonSearchUiState(
                    isError = true,
                    errorMessage = "Not internet connection"
                )
            } else {
                _uiPokemon.value = PokemonSearchUiState(
                    isError = true
                )
            }
        }
    }

    private fun converterPokemonDto(pokemonDto: PokemonDetailDto): PokemonSearchUiData {

        val pokemonUiData =
            PokemonSearchUiData(
                id = pokemonDto.id,
                name = pokemonDto.name,
                image = pokemonDto.imageUrl,
                types = pokemonDto.types.map { it.type.name },
            )

        return pokemonUiData
    }


    companion object {

        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val service = RetrofitClient.retrofitInstance.create(DetailService::class.java)
                return PokemonSearchViewModel(
                    service
                ) as T
            }
        }
    }

}