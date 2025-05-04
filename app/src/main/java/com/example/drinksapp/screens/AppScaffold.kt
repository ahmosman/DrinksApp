package com.example.drinksapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinksapp.components.LogoComponent

@Composable
fun AppScaffold(
    isWelcomeScreen: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B0B3B), Color(0xFF170B3B), Color(0xFF230B3B), Color(0xFF330B3B)
        )
    )

    val cosmosGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF05051E),
            Color(0xFF0B0B28),
            Color(0xFF12093B),
            Color(0xFF1D0A45)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = Color.White,
        topBar = {
            if (!isWelcomeScreen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cosmosGradient)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 24.dp,
                            bottom = 1.dp
                        )
                ) {
                    LogoComponent(horizontal = true)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
        ) {
            content(paddingValues)
        }
    }
}