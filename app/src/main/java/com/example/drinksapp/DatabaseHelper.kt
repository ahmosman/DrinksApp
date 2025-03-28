package com.example.drinksapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.InputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase(context)
        }
    }

    private fun copyDatabase(context: Context) {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        dbPath.parentFile?.mkdirs()

        val inputStream: InputStream = context.assets.open(DATABASE_NAME)
        val outputStream = FileOutputStream(dbPath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Baza danych jest kopiowana z assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aktualizacja bazy danych
    }

    companion object {
        private const val DATABASE_NAME = "drinks.db"
        private const val DATABASE_VERSION = 1
    }

    fun getCocktails(): List<Cocktail> {
        val cocktails = mutableListOf<Cocktail>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT cocktail_id, name FROM cocktails", null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("cocktail_id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                cocktails.add(Cocktail(id, name, "", emptyList()))
            }
        }

        return cocktails
    }

    fun getCocktailDetails(id: Int): Cocktail {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT c.name, c.recipe FROM cocktails c WHERE c.cocktail_id = ?",
            arrayOf(id.toString())
        )

        var name = ""
        var recipe = ""

        cursor.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow("name"))
                recipe = it.getString(it.getColumnIndexOrThrow("recipe"))
            }
        }

        val ingredients = mutableListOf<String>()
        val ingredientsCursor = db.rawQuery(
            "SELECT ingredient_name FROM ingredients WHERE cocktail_id = ?",
            arrayOf(id.toString())
        )

        ingredientsCursor.use {
            while (it.moveToNext()) {
                ingredients.add(it.getString(it.getColumnIndexOrThrow("ingredient_name")))
            }
        }

        return Cocktail(id, name, recipe, ingredients)
    }
}