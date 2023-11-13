package com.example.pulseplanner.model

import java.time.LocalDateTime

class Training {

    var name: String = ""
    var dateTime: LocalDateTime = LocalDateTime.now()
    var exercises: List<TrainingExercise> = emptyList()

    constructor(name: String, dateTime: LocalDateTime, exercises: List<TrainingExercise>) {
        this.name = name
        this.dateTime = dateTime
        this.exercises = exercises
    }

    fun getDurationMinutes(): Int {
        var durationMinutes = 0
        for (exercise in exercises) {
            durationMinutes += exercise.durationMinutes
        }
        return durationMinutes
    }

    fun getCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        for (exercise in exercises) {
            for (category in exercise.categories) {
                if (!categories.contains(category)) {
                    categories.add(category)
                }
            }
        }
        return categories
    }

    override fun toString(): String {

        var str = "Training(name='$name', dateTime=$dateTime \nExercises:\n"

        // format Name - Categories (join with spaces) - Description
        for (exercise in exercises) {
            str += "\t${exercise.name} - ${exercise.categories.joinToString(" ")} - ${exercise.description}\n"
        }

        return str
    }
}
