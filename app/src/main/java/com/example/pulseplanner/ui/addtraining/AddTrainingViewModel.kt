package com.example.pulseplanner.ui.addtraining

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.model.TrainingExercise

class AddTrainingViewModel : ViewModel() {

    private val _trainingExerciseList = MutableLiveData<List<TrainingExercise>>();

    var trainingExerciseList: MutableLiveData<List<TrainingExercise>> = _trainingExerciseList

    init {
        trainingExerciseList.value = emptyList()
    }

    fun updateTrainingExerciseList(newTrainingExerciseList: List<TrainingExercise>) {
        _trainingExerciseList.value = newTrainingExerciseList
    }

    fun addTrainingExercise() {
        val trainingExercise = TrainingExercise()
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

    // update the training exercise with the new duration
    fun updateTrainingExerciseDuration(trainingExercise: TrainingExercise, durationMinuts: Int) {
        val trainingExerciseList = trainingExerciseList.value?.toMutableList()
        val index = trainingExerciseList?.indexOf(trainingExercise)
        trainingExercise.durationMinutes = durationMinuts
        trainingExerciseList?.set(index!!, trainingExercise)
        _trainingExerciseList.value = trainingExerciseList!!
    }
}