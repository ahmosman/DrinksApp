package com.example.drinksapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CocktailListScreen(dbHelper: DatabaseHelper, onCocktailClick: (Cocktail) -> Unit) {
    val cocktails = remember { dbHelper.getCocktails() }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Cocktail List",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(cocktails) { cocktail ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onCocktailClick(cocktail) },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x66FFFFFF)
                    )
                ) {
                    Text(
                        text = cocktail.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}