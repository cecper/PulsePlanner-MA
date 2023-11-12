package com.example.pulseplanner.Repositories

import android.content.Context
import com.example.pulseplanner.model.Training
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileNotFoundException

class TrainingRepository private constructor() {
    private var context: Context? = null

    // Create a new training
    fun createTraining(training: Training) {
        var trainings = getTrainings().toMutableList()
        trainings.add(training)
        saveTrainings(trainings)
        println("Created training: \n$training")
    }

    // Read a single training by name
    fun getTrainingByName(trainingName: String): Training? {
        return getTrainings().find { it.name == trainingName }
    }

    // Delete a training by name
    fun deleteTraining(trainingName: String) {
        var trainings = getTrainings().toMutableList()
        val training = trainings.find { it.name == trainingName }
        if (training != null) {
            trainings.remove(training)
            saveTrainings(trainings)
        }
    }

    // Read all trainings from the JSON file
    fun getTrainings(): List<Training> {
        var fis: FileInputStream? = null

        println("Loading trainings from JSON file")

        try {
            fis = context?.openFileInput("trainings.json")
            val readBytes = fis?.readBytes()
            if (readBytes != null) {
                val json = readBytes.toString(Charsets.UTF_8)
                println("Loaded trainings from JSON file: $json")
                return Gson().fromJson(json, Array<Training>::class.java).toList()
            }
        } catch (e: FileNotFoundException) {
            //context?.showToast("File not found while loading trainings: ${e.message}")
            println("File not found while loading trainings: ${e.message}")
        } finally {
            fis?.close()
        }

        return emptyList()
    }

    // Save trainings to the JSON file
    fun saveTrainings(trainings: List<Training>) {
        val json = Gson().toJson(trainings)
        context?.openFileOutput("trainings.json", Context.MODE_PRIVATE).use {
            it?.write(json.toByteArray())
        }
    }

    companion object {
        private var instance: TrainingRepository? = null

        fun setContext(context: Context) {
            if (instance == null) {
                instance = TrainingRepository()
            }

            instance?.context = context
        }

        fun getInstance(): TrainingRepository {
            if (instance == null) {
                instance = TrainingRepository()
            }

            return instance!!
        }
    }

    fun Context.showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}