package com.example.drinksapp

sealed class Screen {
    object Welcome : Screen()
    object CocktailList : Screen()
    object CocktailDetails : Screen()
}