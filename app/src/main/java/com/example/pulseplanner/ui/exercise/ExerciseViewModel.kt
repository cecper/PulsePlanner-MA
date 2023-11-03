package com.example.pulseplanner.ui.exercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exercise Fragment"
    }
    val text: MutableLiveData<String> = _text
}