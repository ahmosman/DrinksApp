package com.example.drinksapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        if (!context.getDatabasePath(DATABASE_NAME).exists()) {
            copyDatabase(context)
        }
    }

    private fun copyDatabase(context: Context) {
        context.assets.open(DATABASE_NAME).use { inputStream ->
            FileOutputStream(context.getDatabasePath(DATABASE_NAME)).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val DATABASE_NAME = "drinks.db"
        private const val DATABASE_VERSION = 1
    }

    fun getCocktails(): List<Cocktail> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT cocktail_id, name FROM cocktails", null)
        return cursor.use {
            generateSequence { if (it.moveToNext()) it else null }
                .map { Cocktail(it.getInt(0), it.getString(1), "", emptyList()) }
                .toList()
        }
    }

    fun getCocktailDetails(id: Int): Cocktail {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, recipe FROM cocktails WHERE cocktail_id = ?", arrayOf(id.toString()))
        val (name, recipe) = cursor.use { if (it.moveToFirst()) it.getString(0) to it.getString(1) else "" to "" }

        val ingredientsCursor = db.rawQuery("SELECT ingredient_name FROM ingredients WHERE cocktail_id = ?", arrayOf(id.toString()))
        val ingredients = ingredientsCursor.use {
            generateSequence { if (it.moveToNext()) it else null }
                .map { it.getString(0) }
                .toList()
        }

        return Cocktail(id, name, recipe, ingredients)
    }
}