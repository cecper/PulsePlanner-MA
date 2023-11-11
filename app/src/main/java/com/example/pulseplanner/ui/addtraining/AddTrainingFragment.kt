package com.example.pulseplanner.ui.addtraining

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentAddTrainingBinding
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.TrainingExercise
import java.util.Calendar

class AddTrainingFragment : Fragment() {

    private var _binding: FragmentAddTrainingBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTrainingBinding.inflate(inflater, container, false)
        val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)

        // get fields from the layout
        val root: View = binding.root

        val nameField = root.findViewById<EditText>(R.id.nameField)
        val dateField = root.findViewById<EditText>(R.id.dateField)
        val timeField = root.findViewById<EditText>(R.id.timeField)
        val trainingExerciseList = root.findViewById<ListView>(R.id.trainingExerciseList)
        val saveButton = root.findViewById<Button>(R.id.saveButton)
        val addTrainingButton = root.findViewById<Button>(R.id.addTrainingButton)


        //dateField.visibility = View.GONE
        //dateField.visibility = View.VISIBLE

        // Set the date field to today's date
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val day = today.get(Calendar.DAY_OF_MONTH)
        val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
        dateField.setText(formattedDate)

        // Set the time field to the current time
        val hour = today.get(Calendar.HOUR_OF_DAY)
        val minute = today.get(Calendar.MINUTE)
        val formattedTime = String.format("%02d:%02d", hour, minute)
        timeField.setText(formattedTime)

        // Show a date picker when the date field is clicked
        dateField.setOnClickListener {
            showDatePicker(dateField)
        }

        // Show a time picker when the time field is clicked
        timeField.setOnClickListener {
            showTimePicker(timeField)
        }

        // auto update the UI when trainingExerciseList changes
        addTrainingViewModel.trainingExerciseList.observe(viewLifecycleOwner, Observer { newTrainingExerciseList ->
            // Update the UI with the new data when trainingExerciseList changes
            val adapter = trainingExerciseList.adapter as TrainingExerciseAdaptar
            adapter.updateTrainingExerciseList(newTrainingExerciseList)
        })

        addTrainingViewModel.addTrainingExercise()
        addTrainingViewModel.addTrainingExercise()

        addTrainingViewModel.updateTrainingExercise(0, TrainingExercise("Warming up", listOf(Category("Warming up"), Category("Running")), "test description \n dsqfdqskfj\n", 1))
        addTrainingViewModel.updateTrainingExercise(1, TrainingExercise("Game 1", listOf(Category("FH"), Category("Backhand")), "test description \n dsqfdqskfj\n", 5))

        // set the adapter for the training exercise list
        val exerciseList = addTrainingViewModel.trainingExerciseList.value ?: emptyList()
        val adapter = TrainingExerciseAdaptar(requireContext(), exerciseList.toMutableList())
        trainingExerciseList.adapter = adapter
        adapter.setOnDeleteClickListener { deletedExercise ->
            showDeleteConfirmationDialog(deletedExercise)
        }
        adapter.setOnDurationClickListener { durationExercise ->
            showDurationInputDialog(durationExercise)
        }

        // Handle the click event for the "Add Training" button
        addTrainingButton.setOnClickListener {
            addTrainingViewModel.addTrainingExercise()
        }



        return root
    }


    private fun showDatePicker(dateField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Handle the selected date here
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            dateField.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(timeField: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Handle the selected time here
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeField.setText(selectedTime)
            },
            hour,
            minute,
            true // Set to true for 24-hour format or false for 12-hour format
        )

        timePickerDialog.show()
    }

    private fun showDeleteConfirmationDialog(trainingExercise: TrainingExercise) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete the training exercise '${trainingExercise.name}'?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)
            addTrainingViewModel.deleteTrainingExercise(trainingExercise)
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }

    //input dialog for duration minutes
    private fun showDurationInputDialog(trainingExercise: TrainingExercise) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Duration")

        // Set up the layout for the dialog
        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL

        // Create an EditText for input
        val input = EditText(requireContext())
        input.setText(trainingExercise.durationMinutes.toString())
        input.gravity = Gravity.CENTER // Set text alignment to center
        input.inputType = InputType.TYPE_CLASS_NUMBER // Ensure only numeric input

        // Add the EditText to the layout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(50, 0, 50, 0) // Add margins to the EditText
        container.addView(input, layoutParams)

        // Set up positive button (change)
        alertDialogBuilder.setPositiveButton("Change") { dialog, which ->
            val newDuration = input.text.toString().toIntOrNull()
            if (newDuration != null && newDuration > 0) {
                // Valid input, update the exercise duration
                val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)
                addTrainingViewModel.updateTrainingExerciseDuration(trainingExercise, newDuration)
                // You can add further actions here if needed
            } else {
                // Invalid input, show a message or take appropriate action
                Toast.makeText(requireContext(), "Invalid input. Please enter a valid duration.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up negative button (cancel)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Optional: Add any action you want when the user cancels
        }

        // Set up the rest of the dialog properties
        alertDialogBuilder.setMessage("Enter the duration of the exercise in minutes")
        alertDialogBuilder.setView(container)

        // Show the dialog
        alertDialogBuilder.create().show()
    }




}