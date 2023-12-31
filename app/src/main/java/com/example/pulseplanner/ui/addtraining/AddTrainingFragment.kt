package com.example.pulseplanner.ui.addtraining

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.FragmentAddTrainingBinding
import com.example.pulseplanner.model.Exercise
import com.example.pulseplanner.model.Training
import com.example.pulseplanner.model.TrainingExercise
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddTrainingFragment : Fragment() {

    private var _binding: FragmentAddTrainingBinding? = null
    private val binding get() = _binding!!
    private var selectingExercise: TrainingExercise? = null

    @RequiresApi(Build.VERSION_CODES.O)
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
        val addExerciseButton = root.findViewById<Button>(R.id.addExerciseButton)
        val durationText = root.findViewById<TextView>(R.id.durationText)

        // select training
        val gobackButton = root.findViewById<Button>(R.id.goBackButton)
        val exerciseTrainingSearch = root.findViewById<EditText>(R.id.exerciseTrainingSearchField)
        val addExerciseOverview = root.findViewById<ListView>(R.id.addExerciseOverview)

        gobackButton.visibility = View.GONE
        exerciseTrainingSearch.visibility = View.GONE
        addExerciseOverview.visibility = View.GONE


        // Set the date & time field to now & update the total duration
        setDateTimeFieldsToNow()
        updateTotalDuration()

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
        adapter.setOnSelectExerciseClickListener { selectExercise ->
            selectingExercise = selectExercise
            println("Selecting exercise ${selectExercise.name}")

            nameField.visibility = View.GONE
            dateField.visibility = View.GONE
            timeField.visibility = View.GONE
            trainingExerciseList.visibility = View.GONE
            saveButton.visibility = View.GONE
            addExerciseButton.visibility = View.GONE
            durationText.visibility = View.GONE
            gobackButton.visibility = View.VISIBLE
            exerciseTrainingSearch.visibility = View.VISIBLE
            addExerciseOverview.visibility = View.VISIBLE
        }

        // Handle the click event for the "Add Training" button
        addExerciseButton.setOnClickListener {
            addTrainingViewModel.addTrainingExercise()
            updateTotalDuration()
        }

        // handle the click event for the "Save" button
        saveButton.setOnClickListener {
            confirmCreateTraining()
        }

        // select training
        gobackButton.setOnClickListener {
            gobackButton.visibility = View.GONE
            exerciseTrainingSearch.visibility = View.GONE
            addExerciseOverview.visibility = View.GONE
            addExerciseButton.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE
            trainingExerciseList.visibility = View.VISIBLE
            nameField.visibility = View.VISIBLE
            dateField.visibility = View.VISIBLE
            timeField.visibility = View.VISIBLE
            durationText.visibility = View.VISIBLE
        }

        val exerciseOverviewList = addTrainingViewModel.exerciseList.value ?: emptyList()
        val adapterExercise = SelectExerciseAdapter(requireContext(), exerciseOverviewList.toMutableList())
        adapterExercise.setOnSelectExerciseListener { selectExercise ->
            selectExercise(selectExercise)
        }
        addExerciseOverview.adapter = adapterExercise


        addTrainingViewModel.refreshExerciseList()
        addTrainingViewModel.exerciseList.observe(viewLifecycleOwner, Observer { newExerciseList ->
            // Update the UI with the new data when exerciseList changes
            adapterExercise.updateExerciseList(newExerciseList)
        })

        exerciseTrainingSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
                val search = s.toString()

                val exerciseList = addTrainingViewModel.exerciseList.value ?: emptyList()
                val sortedList = exerciseList.sortedByDescending { exercise ->
                    //give points
                    var points = com.example.pulseplanner.util.TextUtils.getSimilarity(exercise.name.toString(), search) * 2

                    for (category in exercise.categories) {
                        points += com.example.pulseplanner.util.TextUtils.getSimilarity(category.categoryName.toString(), search)
                    }

                    points
                }

                addTrainingViewModel.updateExerciseList(sortedList)
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        })

        return root
    }

    // confirm the creation of the training
    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmCreateTraining() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Confirm Creation")
        alertDialogBuilder.setMessage("Are you sure you want to create the training?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            createTraining()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTraining() {
        val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)

        // get fields from the layout
        val root: View = binding.root

        val nameField = root.findViewById<EditText>(R.id.nameField)
        val dateField = root.findViewById<EditText>(R.id.dateField)
        val timeField = root.findViewById<EditText>(R.id.timeField)

        val name = nameField.text.toString()
        val date = dateField.text.toString()
        val time = timeField.text.toString()
        val trainingExerciseList = addTrainingViewModel.trainingExerciseList.value ?: emptyList()

        if (name.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please enter a name for the training",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // date is in the format yyyy-MM-dd (sometimes yyyy-M-d) or yyyy/M/dd (sometimes yyyy/M/d) etc get the in of them all
        // split the date string by - or / and get the first 3 elements
        val dateParts = date.split(Regex("[/-]")).take(3)
        if (dateParts.size != 3) {
            Toast.makeText(requireContext(), "Please enter a valid date", Toast.LENGTH_SHORT).show()
            return
        }

        // time is in the format HH:mm
        val timeParts = time.split(":").take(2)
        if (timeParts.size != 2) {
            Toast.makeText(requireContext(), "Please enter a valid time", Toast.LENGTH_SHORT).show()
            return
        }

        // check if each training exercise has a name (at least one character)
        for (trainingExercise in trainingExerciseList) {
            if (trainingExercise.name == "Select Exercise") {
                Toast.makeText(requireContext(), "Please select an exercise for each training exercise", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val dateTime = LocalDateTime.of(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt(), timeParts[0].toInt(), timeParts[1].toInt())
        val training = Training(name, dateTime, trainingExerciseList)

        TrainingRepository.getInstance().createTraining(training)

        // reset the fields
        nameField.setText("")
        setDateTimeFieldsToNow()
        addTrainingViewModel.clearTrainingExerciseList()
        updateTotalDuration()

        // toast
        Toast.makeText(
            requireContext(),
            "Training '$name' created",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setDateTimeFieldsToNow() {
        val today = Calendar.getInstance()
        val dateField = binding.root.findViewById<EditText>(R.id.dateField)
        val timeField = binding.root.findViewById<EditText>(R.id.timeField)
        dateField.setText(String.format("%04d-%02d-%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH)))
        timeField.setText(String.format("%02d:%02d", today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE)))
    }

    private fun updateTotalDuration() {
        val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)
        val durationText = binding.root.findViewById<TextView>(R.id.durationText)

        val trainingExerciseList = addTrainingViewModel.trainingExerciseList.value ?: emptyList()
        var totalMinutes = 0
        for (trainingExercise in trainingExerciseList) {
            totalMinutes += trainingExercise.durationMinutes
        }

        durationText.text = String.format("Tot: %dh %dm", totalMinutes / 60, totalMinutes % 60)
    }

    private fun selectExercise(exercise: Exercise) {
        val addTrainingViewModel = ViewModelProvider(this).get(AddTrainingViewModel::class.java)
        if (selectingExercise != null) {
            addTrainingViewModel.updateTrainingExercise(selectingExercise!!, exercise)
        }

        // get fields from the layout
        val root: View = binding.root

        val nameField = root.findViewById<EditText>(R.id.nameField)
        val dateField = root.findViewById<EditText>(R.id.dateField)
        val timeField = root.findViewById<EditText>(R.id.timeField)
        val trainingExerciseList = root.findViewById<ListView>(R.id.trainingExerciseList)
        val saveButton = root.findViewById<Button>(R.id.saveButton)
        val addTrainingButton = root.findViewById<Button>(R.id.addExerciseButton)
        val durationText = root.findViewById<TextView>(R.id.durationText)

        // select training
        val gobackButton = root.findViewById<Button>(R.id.goBackButton)
        val exerciseTrainingSearch = root.findViewById<EditText>(R.id.exerciseTrainingSearchField)
        val addExerciseOverview = root.findViewById<ListView>(R.id.addExerciseOverview)

        gobackButton.visibility = View.GONE
        exerciseTrainingSearch.visibility = View.GONE
        addExerciseOverview.visibility = View.GONE

        addTrainingButton.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        trainingExerciseList.visibility = View.VISIBLE
        nameField.visibility = View.VISIBLE
        dateField.visibility = View.VISIBLE
        timeField.visibility = View.VISIBLE
        durationText.visibility = View.VISIBLE
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
                updateTotalDuration()
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