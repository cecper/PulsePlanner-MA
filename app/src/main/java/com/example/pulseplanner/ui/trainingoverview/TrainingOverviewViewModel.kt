package com.example.pulseplanner.ui.trainingoverview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.model.Training

class TrainingOverviewViewModel : ViewModel() {

    private val _trainingList = MutableLiveData<List<Training>>()

    val trainingList: MutableLiveData<List<Training>> = _trainingList

    init {
        refreshTrainingList()
    }

    fun refreshTrainingList() {
        _trainingList.value = TrainingRepository.getInstance().getTrainings()
    }

    fun updateTrainingList(newTrainingList: List<Training>) {
        _trainingList.value = newTrainingList
    }

    fun deleteTraining(training: Training) {
        TrainingRepository.getInstance().deleteTraining(training.name)
        refreshTrainingList()
    }
}