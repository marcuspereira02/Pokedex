package com.marcuspereira.pokedex.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.marcuspereira.pokedex.common.data.remote.RetrofitClient
import com.marcuspereira.pokedex.detail.data.DetailService
import com.marcuspereira.pokedex.detail.data.PokemonDetailDto
import com.marcuspereira.pokedex.detail.presentation.ui.PokemonDetailUiData
import com.marcuspereira.pokedex.detail.presentation.ui.PokemonDetailUiState
import com.marcuspereira.pokedex.detail.presentation.ui.PokemonStatsUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class PokemonDetailViewModel(
    private val service: DetailService
) : ViewModel() {

    private val _uiPokemon = MutableStateFlow(PokemonDetailUiState())
    val uiPokemon: StateFlow<PokemonDetailUiState> = _uiPokemon

    fun fetchPokemonDetail(
        id: String
    ) {
        try {
            _uiPokemon.value = (PokemonDetailUiState(isLoading = true))
            viewModelScope.launch(Dispatchers.IO) {
                val response = service.getDetailPokemon(id)
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    if (pokemon != null) {
                        val pokemonUiData = converterPokemonDto(pokemon)
                        _uiPokemon.value = PokemonDetailUiState(data = pokemonUiData)
                    }
                } else {
                    _uiPokemon.value = PokemonDetailUiState(isError = true)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (ex is UnknownHostException) {
                _uiPokemon.value = PokemonDetailUiState(
                    isError = true,
                    errorMessage = "Not internet connection"
                )
            } else {
                _uiPokemon.value = PokemonDetailUiState(
                    isError = true
                )
            }

        }
    }

    private fun converterPokemonDto(pokemonDto: PokemonDetailDto): PokemonDetailUiData {

        val pokemonUiData =
            PokemonDetailUiData(
                id = pokemonDto.id,
                name = pokemonDto.name,
                image = pokemonDto.imageUrl,
                types = pokemonDto.types.map { it.type.name },
                weight = pokemonDto.weight,
                height = pokemonDto.height,
                stats = pokemonDto.stats.map {
                    PokemonStatsUiData(
                        name = it.stat.name,
                        value = it.baseStat
                    )
                }
            )

        return pokemonUiData
    }

    fun cleanPokemonId() {
        viewModelScope.launch {
            _uiPokemon.value = PokemonDetailUiState()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val service = RetrofitClient.retrofitInstance.create(DetailService::class.java)

                return PokemonDetailViewModel(
                    service
                ) as T
            }
        }
    }
}