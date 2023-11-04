package com.example.pulseplanner.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.CategoryRepository

class ExerciseViewModel : ViewModel() {

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: MutableLiveData<List<Category>> = _categoryList

    private val _selectedCategoriesList = MutableLiveData<List<Category>>()
    val selectedCategoriesList: MutableLiveData<List<Category>> = _selectedCategoriesList
    init {
        refreshCategoryList()
    }

    fun updateCategoryList(newCategoryList: List<Category>) {
        _categoryList.value = newCategoryList
    }

    fun addCategory(categoryName: String) {
        //check if category already exists
        if (CategoryRepository.getInstance().getCategoryByName(categoryName) != null) {
            throw Exception("Category already exists")
        }

        CategoryRepository.getInstance().createCategory(Category(categoryName))
        refreshCategoryList()
    }
    fun addToSelectedCategories(category: Category) {
        val currentSelectedCategories = _selectedCategoriesList.value?.toMutableList() ?: mutableListOf()

        // Check if the category is not already in the selected list
        if (!currentSelectedCategories.contains(category)) {
            currentSelectedCategories.add(category)
            _selectedCategoriesList.value = currentSelectedCategories // Notify LiveData of the change
            println(_selectedCategoriesList.value)
        }
    }

    fun refreshCategoryList() {
        _categoryList.value = CategoryRepository.getInstance().getCategories()
    }


}