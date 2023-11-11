package com.example.pulseplanner.model

class TrainingExercise {
    var name: String = ""
    var categories: List<Category> = emptyList()
    var description: String = ""
    var durationMinutes: Int = 0

    constructor() {}

    constructor(name: String, categories: List<Category>, description: String, durationMinutes: Int) {
        this.name = name
        this.categories = categories
        this.description = description
        this.durationMinutes = durationMinutes
    }

    fun categoryNames(): String {
        var categoryNames = ""
        for (category in categories) {
            categoryNames += category.categoryName + ", "
        }
        return categoryNames
    }

    override fun toString(): String {
        return "TrainingExercise(name='$name', categories=$categories, description='$description', durationMinutes=$durationMinutes"
    }
}