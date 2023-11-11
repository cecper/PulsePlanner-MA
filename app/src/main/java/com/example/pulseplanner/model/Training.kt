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
}
