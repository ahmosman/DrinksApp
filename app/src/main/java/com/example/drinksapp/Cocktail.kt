package com.example.drinksapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    val id: Int,
    val name: String,
    val imageBlob: ByteArray? = null,
    val recipe: String,
    val ingredients: List<String>
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cocktail

        if (id != other.id) return false
        if (name != other.name) return false
        if (recipe != other.recipe) return false
        if (imageBlob != null) {
            if (other.imageBlob == null) return false
            if (!imageBlob.contentEquals(other.imageBlob)) return false
        } else if (other.imageBlob != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + recipe.hashCode()
        result = 31 * result + (imageBlob?.contentHashCode() ?: 0)
        return result
    }
}