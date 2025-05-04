package com.example.drinksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.drinksapp.screens.CocktailDetailsScreen
import com.example.drinksapp.screens.CocktailListScreen
import com.example.drinksapp.screens.Screen
import com.example.drinksapp.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        dbHelper = DatabaseHelper(this)

        setContent {
            DrinksApp(dbHelper)
        }
    }
}

@Composable
fun DrinksApp(dbHelper: DatabaseHelper) {
    var currentScreenIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    val currentScreen = when (currentScreenIndex) {
        0 -> Screen.Welcome
        1 -> Screen.CocktailList
        2 -> Screen.CocktailDetails
        else -> Screen.Welcome
    }

    when (currentScreen) {
        is Screen.Welcome -> WelcomeScreen(
            onShowListClick = { currentScreenIndex = 1 },
            onRandomDrinkClick = {
                val cocktails = dbHelper.getCocktails()
                selectedCocktail = cocktails.random()
                currentScreenIndex = 2
            }
        )

        is Screen.CocktailList -> CocktailListScreen(
            dbHelper = dbHelper,
            onCocktailClick = { cocktail ->
                selectedCocktail = dbHelper.getCocktailDetails(cocktail.id)
                currentScreenIndex = 2
            }, onBackClick = {
                currentScreenIndex = 0
            })

        is Screen.CocktailDetails -> CocktailDetailsScreen(
            cocktail = selectedCocktail!!,
            onBackClick = { currentScreenIndex = 1 })
    }
}