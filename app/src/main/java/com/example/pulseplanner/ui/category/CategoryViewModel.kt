package com.example.pulseplanner.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.model.Category

class CategoryViewModel : ViewModel() {
    private val _categoryList = MutableLiveData<List<Category>>()

    init {
        // Initialize the _categoryList LiveData with some initial data
        _categoryList.value = listOf(
            Category("Chest"),
            Category("Back"),
            Category("Shoulders"),
            Category("Biceps"),
            Category("Triceps"),
            Category("Legs"),
            Category("Abs")
        )
    }

    val categoryList: MutableLiveData<List<Category>> = _categoryList

    fun updateCategoryList(newCategoryList: List<Category>) {
        _categoryList.value = newCategoryList
    }
}