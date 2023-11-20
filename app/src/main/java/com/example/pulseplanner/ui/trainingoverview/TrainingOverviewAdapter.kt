package com.example.pulseplanner.ui.trainingoverview

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.pulseplanner.R
import com.example.pulseplanner.model.Training
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TrainingOverviewAdapter (
    context: Context,
    trainings: MutableList<Training>
) : ArrayAdapter<Training>(context, 0, trainings) {

    private var onViewTrainingListener: ((Training) -> Unit)? = null
    private var onDeletedTrainingListener: ((Training) -> Unit)? = null

    fun setOnViewTrainingListener(listener: (Training) -> Unit) {
        onViewTrainingListener = listener
    }

    fun setOnDeletedTrainingListener(listener: (Training) -> Unit) {
        onDeletedTrainingListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_training, parent, false)
        }

        val training = getItem(position)

        val trainingNameTextView = listItemView?.findViewById<TextView>(R.id.trainingNameTextView)
        val deleteButton = listItemView?.findViewById<TextView>(R.id.deleteButton)
        val trainingCategoriesTextView = listItemView?.findViewById<TextView>(R.id.trainingCategoriesTextView)
        val trainingDurationTextView = listItemView?.findViewById<TextView>(R.id.trainingDurationTextView)
        val trainingDateTimeTextView = listItemView?.findViewById<TextView>(R.id.trainingDateTimeTextView)
        val viewButton = listItemView?.findViewById<TextView>(R.id.viewButton)

        trainingNameTextView?.text = training?.name
        val categoryNames = training?.getCategories()?.map { it.categoryName }?.joinToString(", ")
        trainingCategoriesTextView?.text = categoryNames
        // display duration in format HH:MM
        val durationHours = training?.getDurationMinutes()?.div(60)
        val durationMinutes = training?.getDurationMinutes()?.rem(60)
        trainingDurationTextView?.text = "Duration: ${durationHours}h ${durationMinutes}m"
        // display date in format dd/MM/yyyy HHh mmm
        trainingDateTimeTextView?.text = training?.dateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))


        deleteButton?.setOnClickListener {
            onDeletedTrainingListener?.invoke(training!!)
        }

        viewButton?.setOnClickListener {
            onViewTrainingListener?.invoke(training!!)
        }

        return listItemView!!
    }

    fun updateTrainingList(newTrainingList: List<Training>) {
        clear() // Clear the existing items in the adapter
        addAll(newTrainingList) // Add the updated items to the adapter
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}