package com.example.pulseplanner.Repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pulseplanner.model.Training
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class TrainingRepository private constructor() {
    private var context: Context? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    // Create a new training
    @RequiresApi(Build.VERSION_CODES.O)
    fun createTraining(training: Training) {
        var trainings = getTrainings().toMutableList()
        trainings.add(training)
        saveTrainings(trainings)
        println("Created training: \n$training")
    }

    // Read a single training by name
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTrainingByName(trainingName: String): Training? {
        return getTrainings().find { it.name == trainingName }
    }

    // Delete a training by name
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteTraining(trainingName: String) {
        var trainings = getTrainings().toMutableList()
        val training = trainings.find { it.name == trainingName }
        if (training != null) {
            trainings.remove(training)
            saveTrainings(trainings)
        }
    }

    // Read all trainings from the JSON file
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTrainings(): List<Training> {
        var fis: FileInputStream? = null

        println("Loading trainings from JSON file")

        try {
            fis = context?.openFileInput("trainings.json")
            val readBytes = fis?.readBytes()
            if (readBytes != null) {
                val json = readBytes.toString(Charsets.UTF_8)
                println("Loaded trainings from JSON file: $json")

                val typeToken = object : TypeToken<List<Training>>() {}.type
                return gson.fromJson(json, typeToken)
            }
        } catch (e: FileNotFoundException) {
            //context?.showToast("File not found while loading trainings: ${e.message}")
            println("File not found while loading trainings: ${e.message}")
        } finally {
            fis?.close()
        }

        return emptyList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTrainingsOfDate(date: LocalDate): List<Training> {
        var fis: FileInputStream? = null

        try {
            fis = context?.openFileInput("trainings.json")
            val readBytes = fis?.readBytes()
            if (readBytes != null) {
                val json = readBytes.toString(Charsets.UTF_8)
                println("Loaded trainings from JSON file: $json")

                val typeToken = object : TypeToken<List<Training>>() {}.type
                val trainings = gson.fromJson<List<Training>>(json, typeToken)

                val filteredTrainings = trainings.filter { training ->
                    val trainingDateTime = training.dateTime
                    val trainingDate = trainingDateTime.toLocalDate()
                    trainingDate == date
                }

                return filteredTrainings.sortedBy { it.dateTime.toLocalTime() }
            }
        } catch (e: FileNotFoundException) {
            println("File not found while loading trainings: ${e.message}")
        } finally {
            fis?.close()
        }

        return emptyList()
    }

    // Save trainings to the JSON file
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveTrainings(trainings: List<Training>) {
        val json = gson.toJson(trainings)
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

    // Custom Gson adapter for LocalDateTime
    private class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>,
        JsonDeserializer<LocalDateTime> {
        @RequiresApi(Build.VERSION_CODES.O)
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        @RequiresApi(Build.VERSION_CODES.O)
        override fun serialize(
            src: LocalDateTime?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(formatter.format(src))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LocalDateTime {
            return LocalDateTime.parse(json!!.asString, formatter)
        }
    }
}