package com.example.pulseplanner.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.CategoryRepository

class CategoryViewModel : ViewModel() {
    private val _categoryList = MutableLiveData<List<Category>>()

    init {
        refreshCategoryList()
    }

    val categoryList: MutableLiveData<List<Category>> = _categoryList

    fun updateCategoryList(newCategoryList: List<Category>) {
        _categoryList.value = newCategoryList
    }

    fun deleteCategory(category: Category) {
        CategoryRepository.getInstance().deleteCategory(category.categoryName)
        refreshCategoryList()
    }

    fun addCategory(categoryName: String) {
        //check if category already exists
        if (CategoryRepository.getInstance().getCategoryByName(categoryName) != null) {
            throw Exception("Category already exists")
        }

        CategoryRepository.getInstance().createCategory(Category(categoryName))
        refreshCategoryList()
    }

    fun refreshCategoryList() {
        _categoryList.value = CategoryRepository.getInstance().getAllCategories()
    }
}