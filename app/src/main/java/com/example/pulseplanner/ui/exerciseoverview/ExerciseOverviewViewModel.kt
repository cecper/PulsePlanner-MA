package com.example.pulseplanner.ui.exerciseoverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.Repositories.ExerciseRepository
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.Exercise

class ExerciseOverviewViewModel : ViewModel() {

    private val _categoryList = MutableLiveData<List<Exercise>>()

    val exerciseList: MutableLiveData<List<Exercise>> = _categoryList

    init {
        refreshExerciseList()
    }

    fun refreshExerciseList() {
        _categoryList.value = ExerciseRepository.getInstance().getExercises()
    }

    fun deleteExercise(exercise: Exercise) {
        ExerciseRepository.getInstance().deleteExercise(exercise.name)
        refreshExerciseList()
    }
}