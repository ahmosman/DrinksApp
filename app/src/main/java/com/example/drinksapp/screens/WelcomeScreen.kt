package com.example.drinksapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinksapp.components.LogoComponent

@Composable
fun WelcomeScreen(
    onShowListClick: () -> Unit,
    onRandomDrinkClick: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    AppScaffold(
        isWelcomeScreen = true,
        currentRoute = currentRoute,
        onNavigate = onNavigate
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoComponent(horizontal = false)

            Button(
                onClick = onShowListClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Text("Show list")
            }

            Button(
                onClick = onRandomDrinkClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                ),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Random drink")
            }
        }
    }
}