package com.marcuspereira.pokedex.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TryAgainButton(
    onRetry : () -> Unit
){
    OutlinedButton(
        onClick = onRetry,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE53935)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFE53935)
        ),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 10.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = "Try again",
            fontWeight = FontWeight.SemiBold
        )
    }
}

