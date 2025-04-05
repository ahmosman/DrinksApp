package com.example.drinksapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drinksapp.Cocktail
import com.example.drinksapp.components.TimerComponent

@Composable
fun CocktailDetailsScreen(cocktail: Cocktail, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cocktail.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Text("Back")
            }
        }

        Text(
            text = "Ingredients:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        cocktail.ingredients.forEach { ingredient ->
            Text(
                text = "• $ingredient",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Text(
            text = "Recipe:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = cocktail.recipe,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.3f)
        )

        TimerComponent(
            showTitle = false,
        )
    }
}