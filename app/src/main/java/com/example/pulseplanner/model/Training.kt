package com.example.pulseplanner.model

import java.time.LocalDateTime

class Training {

    var name: String = ""
    var dateTime: LocalDateTime = LocalDateTime.now()
    var exercises: List<ExerciseTraining> = emptyList()

    constructor(name: String, dateTime: LocalDateTime, exercises: List<ExerciseTraining>) {
        this.name = name
        this.dateTime = dateTime
        this.exercises = exercises
    }
}
