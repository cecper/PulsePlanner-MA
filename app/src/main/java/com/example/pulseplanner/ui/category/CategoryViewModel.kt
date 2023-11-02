package com.example.pulseplanner.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.CategoryRepository

class CategoryViewModel : ViewModel() {
    private val _categoryList = MutableLiveData<List<Category>>()

    init {
        CategoryRepository.getInstance().getAllCategories().let {
            _categoryList.value = it
        }
    }

    val categoryList: MutableLiveData<List<Category>> = _categoryList

    fun updateCategoryList(newCategoryList: List<Category>) {
        _categoryList.value = newCategoryList
    }

    fun deleteCategory(category: Category) {
        val currentList = _categoryList.value ?: emptyList()
        val newList = currentList.toMutableList()
        newList.remove(category)
        _categoryList.value = newList
    }
}