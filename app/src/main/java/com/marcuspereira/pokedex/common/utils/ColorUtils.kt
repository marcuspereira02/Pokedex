package com.marcuspereira.pokedex.common.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun getTextColor(background: Color): Color {
    return if (background.luminance() < 0.5f) {
        Color.White
    } else {
        Color.Black
    }
}