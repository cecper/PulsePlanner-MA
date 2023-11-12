package com.example.pulseplanner.Repositories

import android.content.Context
import android.widget.Toast
import com.example.pulseplanner.model.Exercise
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class ExerciseRepository private constructor() {
    private var context: Context? = null

    // Create a new exercise
    fun createExercise(exercise: Exercise) {
        var exercises = getExercises().toMutableList()
        exercises.add(exercise)
        saveExercises(exercises)
    }

    // Read a single exercise by name
    fun getExerciseByName(exerciseName: String): Exercise? {
        return getExercises().find { it.name == exerciseName }
    }

    // Delete an exercise by name
    fun deleteExercise(exerciseName: String) {
        var exercises = getExercises().toMutableList()
        val exercise = exercises.find { it.name == exerciseName }
        if (exercise != null) {
            exercises.remove(exercise)
            saveExercises(exercises)
        }
    }

    // Read all exercises from the JSON file
    fun getExercises(): List<Exercise> {
        var fis: FileInputStream? = null

        println("Loading exercises from JSON file")

        try {
            fis = context?.openFileInput("exercises.json")
            val readBytes = fis?.readBytes()
            if (readBytes != null) {
                val json = readBytes.toString(Charsets.UTF_8)
                println("Loaded exercises from JSON file: $json")
                return Gson().fromJson(json, Array<Exercise>::class.java).toList()
            }
        } catch (e: FileNotFoundException) {
            //context?.showToast("File not found while loading exercises: ${e.message}")
            println("File not found while loading exercises: ${e.message}")
        } finally {
            fis?.close()
        }

        return emptyList()
    }

    // Save exercises to the JSON file
    private fun saveExercises(exercises : List<Exercise>) {
        val json = Gson().toJson(exercises)
        var fos: FileOutputStream? = null

        println("Saving exercises to JSON file: $json")

        try {
            fos = context?.openFileOutput("exercises.json", Context.MODE_PRIVATE)
            fos?.write(json.toByteArray())
        } catch (e: FileNotFoundException) {
            context?.showToast("File not found: ${e.message}")
            println("File not found: ${e.message}")
        } finally {
            fos?.close()
        }
    }

    companion object {
        private var instance: ExerciseRepository? = null

        fun setContext(context: Context) {
            if (instance == null) {
                instance = ExerciseRepository()
            }
            instance?.context = context
        }

        fun getInstance(): ExerciseRepository {
            if (instance == null) {
                instance = ExerciseRepository()
            }
            return instance!!
        }
    }

    // toasts
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }
}
