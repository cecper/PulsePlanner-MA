package com.example.pulseplanner.ui.trainingoverview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pulseplanner.R
import com.example.pulseplanner.model.TrainingExercise

class TrainingExerciseListAdapter (
    context: Context,
    trainingExercises: MutableList<TrainingExercise>,
) : ArrayAdapter<TrainingExercise>(context, 0, trainingExercises) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView =
                LayoutInflater.from(context)
                    .inflate(R.layout.list_item_training_overview_exercises, parent, false)
        }

        val trainingExercise = getItem(position)

        val exerciseName = listItemView?.findViewById<TextView>(R.id.trainingExerciseName)
        val exerciseDuration = listItemView?.findViewById<TextView>(R.id.trainingExerciseDuration)
        val exerciseCategories =
            listItemView?.findViewById<TextView>(R.id.trainingExerciseCategories)
        val exerciseDescription =
            listItemView?.findViewById<TextView>(R.id.trainingExerciseDescription)

        exerciseName?.text = trainingExercise?.name
        // display duration as ..h ..min if duration is more than 60 minutes otherwise display only minutes
        val durationMinutes = trainingExercise?.durationMinutes
        val durationHours = durationMinutes?.div(60)
        val durationMinutesRemaining = durationMinutes?.rem(60)
        exerciseDuration?.text =
            "Duration: ${if (durationHours != 0) "${durationHours}h " else ""}${durationMinutesRemaining}min"
        exerciseCategories?.text =
            "${trainingExercise?.categories?.map { it.categoryName }?.joinToString(", ")}"
        exerciseDescription?.text = "${trainingExercise?.description}"
        val categoryNames =
            trainingExercise?.categories?.map { it.categoryName }?.joinToString(", ")
        exerciseCategories?.text = categoryNames

        return listItemView!!
    }

    fun updateTrainingExerciseList(newTrainingExerciseList: List<TrainingExercise>) {
        clear() // Clear the existing items in the adapter
        addAll(newTrainingExerciseList) // Add the updated items to the adapter
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
