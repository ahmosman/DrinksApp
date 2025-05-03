package com.example.drinksapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.InputStream

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val cocktails = mutableListOf<Cocktail>()

    init {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase(context)
        }
        loadCocktails()
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

    private fun loadCocktails() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT cocktail_id, name, recipe, image FROM cocktails", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val name = it.getString(1)
                val recipe = it.getString(2)
                val image = if (!it.isNull(3)) it.getBlob(3) else null
                val ingredientsCursor = db.rawQuery(
                    "SELECT ingredient_name FROM ingredients WHERE cocktail_id = ?",
                    arrayOf(id.toString())
                )
                val ingredients = ingredientsCursor.use { ingCursor ->
                    generateSequence { if (ingCursor.moveToNext()) ingCursor else null }
                        .map { ingCursor.getString(0) }
                        .toList()
                }
                cocktails.add(Cocktail(id, name, image, recipe, ingredients))
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