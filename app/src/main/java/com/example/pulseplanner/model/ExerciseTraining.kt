package com.example.pulseplanner.model

class ExerciseTraining : Exercise {
    val id = 0
    val durationSeconds: Int = 0

    constructor(name: String, categories: List<Category>, description: String) : super(name, categories, description) {

    }
}
