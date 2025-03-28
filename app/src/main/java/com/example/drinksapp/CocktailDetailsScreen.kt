package com.example.drinksapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CocktailDetailsScreen(cocktail: Cocktail, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        Text(
            text = cocktail.name,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            fontSize = 16.sp
        )
    }
}