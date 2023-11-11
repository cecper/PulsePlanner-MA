package com.example.pulseplanner.ui.addtraining

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pulseplanner.R
import com.example.pulseplanner.model.TrainingExercise

class TrainingExerciseAdaptar (
    context: Context,
    private var trainingExerciseList: MutableList<TrainingExercise>
) : ArrayAdapter<TrainingExercise>(context, R.layout.list_item_training_exercise, trainingExerciseList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_training_exercise, parent, false)

        val item = getItem(position)
        val name = view.findViewById<TextView>(R.id.trainingExerciseName)
        val categories = view.findViewById<TextView>(R.id.trainingExerciseCategories)
        val description = view.findViewById<TextView>(R.id.trainingExerciseDescription)
        val durationSeconds = view.findViewById<TextView>(R.id.trainingExerciseDurationSeconds)
        val durationMinutes = view.findViewById<TextView>(R.id.trainingExerciseDurationMinutes)

        name.text = item?.name
        categories.text = item?.categoryNames()
        description.text = item?.description
        durationSeconds.text = item?.durationSeconds.toString()
        durationMinutes.text = item?.durationMinutes.toString()

        return view
    }

    // Update the training exercise list when it changes.
    fun updateTrainingExerciseList(newTrainingExerciseList: List<TrainingExercise>) {
        clear()
        addAll(newTrainingExerciseList)
        notifyDataSetChanged()
    }
}