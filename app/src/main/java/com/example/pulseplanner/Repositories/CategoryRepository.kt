package com.example.pulseplanner.model

import android.os.Environment
import com.google.gson.Gson
import java.io.File
import kotlin.io.path.Path

class CategoryRepository private constructor(private val jsonFilePath: String) {
    private val categories = mutableListOf<Category>()

    init {
        // Load existing categories from a JSON file if available
        loadCategories()
    }

    // Create a new category
    fun createCategory(category: Category) {
        categories.add(category)
        saveCategories()
    }

    // Read all categories
    fun getAllCategories(): List<Category> {
        return categories.toList()
    }

    // Read a single category by name
    fun getCategoryByName(categoryName: String): Category? {
        return categories.find { it.categoryName == categoryName }
    }

    // Update a category by name
    fun updateCategory(categoryName: String, updatedCategory: Category) {
        val categoryIndex = categories.indexOfFirst { it.categoryName == categoryName }
        if (categoryIndex != -1) {
            categories[categoryIndex] = updatedCategory
            saveCategories()
        }
    }

    // Delete a category by name
    fun deleteCategory(categoryName: String) {
        val category = categories.find { it.categoryName == categoryName }
        if (category != null) {
            categories.remove(category)
            saveCategories()
        }
    }

    // Save categories to the JSON file
    private fun saveCategories() {
        val json = Gson().toJson(categories)
        File(jsonFilePath).writeText(json)
    }

    // Load categories from the JSON file
    private fun loadCategories() {
        val json = File(jsonFilePath).readText()
        categories.addAll(Gson().fromJson(json, Array<Category>::class.java).toList())
    }

    companion object {
        private var instance: CategoryRepository? = null
        private val dirPath = Environment.getDataDirectory().absolutePath + File.separator + "pulseplanner"
        private val jsonFilePath = dirPath + File.separator + "categories.json"

        fun getInstance(): CategoryRepository {
            if (instance == null) {

                if (!File(dirPath).isDirectory)
                    File(dirPath).mkdirs()

                println("dir: $dirPath" )
                println("file: $jsonFilePath")

                if (!File(jsonFilePath).exists())
                    File(jsonFilePath).createNewFile()

                instance = CategoryRepository(jsonFilePath)
            }
            return instance!!
        }
    }
}
