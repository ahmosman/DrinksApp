package com.example.drinksapp.screens

sealed class Screen {
    object Welcome : Screen()
    object CocktailList : Screen()
    object CocktailDetails : Screen()
}