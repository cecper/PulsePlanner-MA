package com.example.pulseplanner.ui.addtraining

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.pulseplanner.R
import com.example.pulseplanner.model.Exercise
import com.example.pulseplanner.model.TrainingExercise

class SelectExerciseAdapter (
    context: Context,
    exercises: MutableList<Exercise>,
) : ArrayAdapter<Exercise>(context, 0, exercises) {

    private var onSelectExerciseListener: ((Exercise) -> Unit)? = null

    fun setOnSelectExerciseListener(listener: (Exercise) -> Unit) {
        onSelectExerciseListener = listener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView =
                LayoutInflater.from(context).inflate(R.layout.list_select_exercise, parent, false)
        }

        val exercise = getItem(position)

        val exerciseNameTextView = listItemView?.findViewById<TextView>(R.id.selectExerciseNameTextView)
        val exerciseDescriptionTextView = listItemView?.findViewById<TextView>(R.id.selectExerciseDescriptionTextView)
        val exerciseCategoriesTextView = listItemView?.findViewById<TextView>(R.id.selectExerciseCategoriesTextView)
        val selectButton = listItemView?.findViewById<TextView>(R.id.selectButton)

        exerciseNameTextView?.text = exercise?.name
        exerciseDescriptionTextView?.text = "description: "+exercise?.description
        val categoryNames = exercise?.categories?.map { it.categoryName }?.joinToString(", ")
        exerciseCategoriesTextView?.text = "Categories: "+categoryNames
        selectButton?.setOnClickListener {
            Toast.makeText(context, "Exercise ${exercise?.name} selected", Toast.LENGTH_SHORT).show()
            onSelectExerciseListener?.invoke(exercise!!)
        }

        return listItemView!!
    }

    fun updateExerciseList(newExerciseList: List<Exercise>) {
        clear() // Clear the existing items in the adapter
        addAll(newExerciseList) // Add the updated items to the adapter
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

}