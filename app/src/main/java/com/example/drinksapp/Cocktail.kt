package com.example.drinksapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    val id: Int,
    val name: String,
    val recipe: String,
    val ingredients: List<String>
) : Parcelable