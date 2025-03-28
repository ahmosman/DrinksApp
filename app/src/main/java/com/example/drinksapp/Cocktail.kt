package com.example.drinksapp

data class Cocktail(
    val id: Int,
    val name: String,
    val recipe: String,
    val ingredients: List<String>
)