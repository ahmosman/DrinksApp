package com.example.drinksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.drinksapp.screens.CategoriesScreen
import com.example.drinksapp.screens.CocktailDetailsScreen
import com.example.drinksapp.screens.CocktailListScreen
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
    var currentRoute by rememberSaveable { mutableStateOf("welcome") }
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    val handleNavigation: (String) -> Unit = { route ->
        if (route == "random") {
            val cocktails = dbHelper.getCocktails()
            if (cocktails.isNotEmpty()) {
                selectedCocktail = cocktails.random()
                currentRoute = "details"
            }
        } else {
            currentRoute = route
        }
    }

    when {
        currentRoute == "welcome" -> WelcomeScreen(
            onShowListClick = { handleNavigation("list?category=all") },
            onRandomDrinkClick = { handleNavigation("random") },
            currentRoute = currentRoute,
            onNavigate = handleNavigation
        )

        currentRoute == "categories" -> CategoriesScreen(
            currentRoute = currentRoute,
            onNavigate = handleNavigation,
            dbHelper = dbHelper
        )

        currentRoute.startsWith("list") -> CocktailListScreen(
            dbHelper = dbHelper,
            onCocktailClick = { cocktail ->
                selectedCocktail = dbHelper.getCocktailDetails(cocktail.id)
                currentRoute = "details"
            },
            currentRoute = currentRoute,
            onNavigate = handleNavigation
        )

        currentRoute == "details" -> CocktailDetailsScreen(
            cocktail = selectedCocktail!!,
            currentRoute = currentRoute,
            onNavigate = handleNavigation
        )
    }
}