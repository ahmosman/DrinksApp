package com.example.drinksapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val cocktails = mutableListOf<Cocktail>()

    init {
        loadCocktails()
    }

    private fun loadCocktails() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT cocktail_id, name, recipe FROM cocktails", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val name = it.getString(1)
                val recipe = it.getString(2)
                val ingredientsCursor = db.rawQuery(
                    "SELECT ingredient_name FROM ingredients WHERE cocktail_id = ?",
                    arrayOf(id.toString())
                )
                val ingredients = ingredientsCursor.use { ingCursor ->
                    generateSequence { if (ingCursor.moveToNext()) ingCursor else null }
                        .map { ingCursor.getString(0) }
                        .toList()
                }
                cocktails.add(Cocktail(id, name, recipe, ingredients))
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val DATABASE_NAME = "drinks.db"
        private const val DATABASE_VERSION = 1
    }

    fun getCocktails(): List<Cocktail> = cocktails

    fun getCocktailDetails(id: Int): Cocktail? = cocktails.find { it.id == id }
}