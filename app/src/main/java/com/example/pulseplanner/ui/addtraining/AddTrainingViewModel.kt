package com.example.pulseplanner.ui.addtraining

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.Repositories.ExerciseRepository
import com.example.pulseplanner.model.Exercise
import com.example.pulseplanner.model.TrainingExercise

class AddTrainingViewModel : ViewModel() {

    private val _trainingExerciseList = MutableLiveData<List<TrainingExercise>>();
    private val _categoryList = MutableLiveData<List<Exercise>>()

    var trainingExerciseList: MutableLiveData<List<TrainingExercise>> = _trainingExerciseList
    val exerciseList: MutableLiveData<List<Exercise>> = _categoryList

    init {
        trainingExerciseList.value = emptyList()
        refreshExerciseList()
    }

    fun refreshExerciseList() {
        _categoryList.value = ExerciseRepository.getInstance().getExercises()
    }

    fun updateExerciseList(newExerciseList: List<Exercise>) {
        _categoryList.value = newExerciseList
    }

    fun updateTrainingExerciseList(newTrainingExerciseList: List<TrainingExercise>) {
        _trainingExerciseList.value = newTrainingExerciseList
    }

    fun addTrainingExercise() {
        val trainingExercise = TrainingExercise("Select Exercise", emptyList(), "to be selected ...", 5)
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        trainingExerciseList?.add(trainingExercise)
        _trainingExerciseList.value = trainingExerciseList!!
    }

    fun deleteTrainingExercise(trainingExercise: TrainingExercise) {
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        trainingExerciseList?.remove(trainingExercise)
        _trainingExerciseList.value = trainingExerciseList!!
    }

    fun updateTrainingExercise(index: Int, trainingExercise: TrainingExercise) {
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        trainingExerciseList?.set(index, trainingExercise)
        _trainingExerciseList.value = trainingExerciseList!!
    }

    fun updateTrainingExercise(trainingExercise: TrainingExercise, selectExercise: Exercise) {
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        val index = trainingExerciseList?.indexOf(trainingExercise)
        trainingExercise.name = selectExercise.name
        trainingExercise.description = selectExercise.description
        trainingExercise.categories = selectExercise.categories
        if (index != null) {
            trainingExerciseList?.set(index, trainingExercise)
        }
        _trainingExerciseList.value = trainingExerciseList!!
    }

    // update the training exercise with the new duration
    fun updateTrainingExerciseDuration(trainingExercise: TrainingExercise, durationMinuts: Int) {
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        val index = trainingExerciseList?.indexOf(trainingExercise)
        trainingExercise.durationMinutes = durationMinuts
        trainingExerciseList?.set(index!!, trainingExercise)
        _trainingExerciseList.value = trainingExerciseList!!
    }
}