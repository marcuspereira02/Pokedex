package com.marcuspereira.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.marcuspereira.pokedex.list.presentation.PokemonListViewModel
import com.marcuspereira.pokedex.ui.theme.PokedexTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val listViewModel by viewModels<PokemonListViewModel> { PokemonListViewModel.factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  PokedexApp(
                      listViewModel
                  )
                }
            }
        }
    }
}
