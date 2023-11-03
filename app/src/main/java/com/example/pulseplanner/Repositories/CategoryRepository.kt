package com.example.pulseplanner.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class CategoryRepository private constructor() {
    private var context: Context? = null

    // Create a new category
    fun createCategory(category: Category) {
        var categories = getCategories().toMutableList()
        categories.add(category)
        saveCategories(categories)
    }

    // Read all categories
    fun getAllCategories(): List<Category> {
        return getCategories().toList()
    }

    // Read a single category by name
    fun getCategoryByName(categoryName: String): Category? {
        return getCategories().find { it.categoryName == categoryName }
    }

    // Delete a category by name
    fun deleteCategory(categoryName: String) {
        var categories = getCategories().toMutableList()
        val category = categories.find { it.categoryName == categoryName }
        if (category != null) {
            categories.remove(category)
            saveCategories(categories)
        }
    }

    // Save categories to the JSON file
    private fun saveCategories(categories : List<Category>) {
        val json = Gson().toJson(categories)
        var fos: FileOutputStream? = null

        println("Saving categories to JSON file: $json")

        try {
            fos = context?.openFileOutput("categories.json", MODE_PRIVATE)
            fos?.write(json.toByteArray())
        } catch (e: FileNotFoundException) {
            context?.showToast("File not found: ${e.message}")
            println("File not found: ${e.message}")
        } finally {
            fos?.close()
        }
    }

    private fun getCategories(): List<Category> {
        var fis: FileInputStream? = null

        println("Loading categories from JSON file")

        try {
            fis = context?.openFileInput("categories.json")
            val readBytes = fis?.readBytes()
            println("File contents: $readBytes")
            if (readBytes != null) {
                val json = readBytes.toString(Charsets.UTF_8)
                return Gson().fromJson(json, Array<Category>::class.java).toList()
            }
        } catch (e: FileNotFoundException) {
            context?.showToast("File not found while loading categories: ${e.message}")
            println("File not found while loading categories: ${e.message}")
        } finally {
            fis?.close()
        }

        return emptyList()
    }

    companion object {
        private var instance: CategoryRepository? = null

        fun getInstance(): CategoryRepository {
            if (instance == null) {
                instance = CategoryRepository()
            }
            return instance!!
        }

        fun setContext(context: Context) {
            this.getInstance().context = context
        }
    }

    // Function to show a toast message
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
