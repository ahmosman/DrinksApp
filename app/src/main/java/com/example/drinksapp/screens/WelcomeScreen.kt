package com.example.drinksapp.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.drinksapp.components.AnimatedLogoComponent
import com.example.drinksapp.components.LogoComponent
import androidx.compose.runtime.getValue

@Composable
fun WelcomeScreen(
    onShowListClick: () -> Unit,
    onRandomDrinkClick: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var animationFinished by remember { mutableStateOf(false) }

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
            if (animationFinished) {
                LogoComponent(horizontal = false, compact = isLandscape)
            } else {
                AnimatedLogoComponent(
                    compact = isLandscape,
                    onAnimationEnd = { animationFinished = true }
                )
            }

            if (isLandscape) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Button(
                        onClick = onShowListClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text("Show list")
                    }

                    Button(
                        onClick = { onNavigate("categories") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text("Categories")
                    }

                    Button(
                        onClick = onRandomDrinkClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                    ) {
                        Text("Random drink")
                    }
                }
            } else {
                Button(
                    onClick = onShowListClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Text("Show list")
                }

                Button(
                    onClick = { onNavigate("categories") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Categories")
                }

                Button(
                    onClick = onRandomDrinkClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Random drink")
                }
            }
        }
    }
}