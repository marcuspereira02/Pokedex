package com.marcuspereira.pokedex.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcuspereira.pokedex.R

@Composable
fun RetryErrorContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            modifier = Modifier.padding(16.dp),
            text = errorMessage,
            fontSize = 16.sp,
            color = Color.Red
        )

        TryAgainButton (onRetry = onRetry)

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sad_pokeball),
                contentDescription = "Sad Pokeball Image",
                tint = Color.Unspecified
            )
        }
    }
}