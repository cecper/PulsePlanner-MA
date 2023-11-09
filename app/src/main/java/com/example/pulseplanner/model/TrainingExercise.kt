package com.example.pulseplanner.model

class TrainingExercise {
    var name: String = ""
    var categories: List<Category> = emptyList()
    var description: String = ""
    var durationMinutes: Int = 0
    var durationSeconds: Int = 0

    constructor() {}

    constructor(name: String, categories: List<Category>, description: String, durationMinutes: Int, durationSeconds: Int) {
        this.name = name
        this.categories = categories
        this.description = description
        this.durationMinutes = durationMinutes
        this.durationSeconds = durationSeconds
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getCategories(): List<Category> {
        return categories
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getDurationMinutes(): Int {
        return durationMinutes
    }

    fun setDurationMinutes(durationMinutes: Int) {
        this.durationMinutes = durationMinutes
    }

    fun getDurationSeconds(): Int {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Int) {
        this.durationSeconds = durationSeconds
    }

    override fun toString(): String {
        return "TrainingExercise(name='$name', categories=$categories, description='$description', durationMinutes=$durationMinutes, durationSeconds=$durationSeconds)"
    }
}