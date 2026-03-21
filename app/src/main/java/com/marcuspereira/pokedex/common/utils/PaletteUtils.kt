package com.marcuspereira.pokedex.common.utils

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

fun extractColorFromDrawable(drawable: Drawable): Color {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val palette = Palette.from(bitmap).generate()

    val swatch = palette.dominantSwatch ?: palette.vibrantSwatch
    return swatch?.let { Color(it.rgb) } ?: Color.LightGray
}