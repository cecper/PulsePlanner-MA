package com.example.pulseplanner.ui.trainingoverview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.FragmentTrainingOverviewBinding
import com.example.pulseplanner.model.Training
import java.time.format.DateTimeFormatter

class TrainingOverviewFragment : Fragment() {
    private var _binding: FragmentTrainingOverviewBinding? = null

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingOverviewBinding.inflate(inflater, container, false)
        val trainingOverviewViewModel = ViewModelProvider(this).get(TrainingOverviewViewModel::class.java)

        val root: View = binding.root

        // all trainings
        val trainingList = root.findViewById<android.widget.ListView>(com.example.pulseplanner.R.id.trainingOverviewListView)

        // single training
        val goBackButton = root.findViewById<Button>(com.example.pulseplanner.R.id.goBackButton)
        val trainingNameTextView = root.findViewById<TextView>(com.example.pulseplanner.R.id.trainingOverviewNameTextView)
        val durationTextTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.durationTextTrainingOverview)
        val dateTimeFieldTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.dateTimeFieldTrainingOverview)
        val categoriesTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.categoriesTrainingOverview)
        val trainingExerciseList = root.findViewById<ListView>(com.example.pulseplanner.R.id.trainingExerciseList)

        setAllTrainingMode(true)


        trainingOverviewViewModel.refreshTrainingList()

        val adapter2 = TrainingExerciseListAdapter(
            requireContext(),
            mutableListOf()
        )
        trainingExerciseList.adapter = adapter2

        val adapter = TrainingOverviewAdapter(
            requireContext(),
            trainingOverviewViewModel.trainingList.value!!.toMutableList()
        )
        adapter.setOnDeletedTrainingListener { training ->
            showDeleteConfirmationDialog(training)
        }
        adapter.setOnViewTrainingListener { training ->
            adapter2.updateTrainingExerciseList(training.exercises)
            println("training name: " + training.name)
            trainingNameTextView.text = training.name
            // display duration as ..h ..min if duration is more than 60 minutes otherwise display only minutes
            val durationMinutes = training.getDurationMinutes()
            val durationHours = durationMinutes.div(60)
            val durationMinutesRemaining = durationMinutes.rem(60)
            durationTextTrainingOverview.text =
                "Tot: ${if (durationHours != 0) "${durationHours}h " else ""}${durationMinutesRemaining}min"
            // display dateTime in format dd/MM/yyyy HHh mmm
            dateTimeFieldTrainingOverview.text = training.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            val categoryNames = training.getCategories().map { it.categoryName }.joinToString(", ")
            categoriesTrainingOverview.text = categoryNames
            setAllTrainingMode(false)
        }
        trainingList.adapter = adapter

        trainingOverviewViewModel.trainingList.observe(viewLifecycleOwner) {
            adapter.updateTrainingList(it.toMutableList())
        }

        goBackButton.setOnClickListener {
            setAllTrainingMode(true)
        }

        return root
    }

    private fun setAllTrainingMode(boolean: Boolean) {
        val root: View = binding.root

        // all trainings
        val trainingList = root.findViewById<android.widget.ListView>(com.example.pulseplanner.R.id.trainingOverviewListView)

        // single training
        val goBackButton = root.findViewById<Button>(com.example.pulseplanner.R.id.goBackButton)
        val trainingNameTextView = root.findViewById<TextView>(com.example.pulseplanner.R.id.trainingOverviewNameTextView)
        val durationTextTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.durationTextTrainingOverview)
        val dateFieldTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.dateTimeFieldTrainingOverview)
        val categoriesTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.categoriesTrainingOverview)
        val trainingExerciseList = root.findViewById<ListView>(com.example.pulseplanner.R.id.trainingExerciseList)

        if (boolean) {
            trainingList.visibility = View.VISIBLE
            goBackButton.visibility = View.GONE
            trainingNameTextView.visibility = View.GONE
            durationTextTrainingOverview.visibility = View.GONE
            dateFieldTrainingOverview.visibility = View.GONE
            categoriesTrainingOverview.visibility = View.GONE
            trainingExerciseList.visibility = View.GONE
        } else {
            trainingList.visibility = View.GONE
            goBackButton.visibility = View.VISIBLE
            trainingNameTextView.visibility = View.VISIBLE
            durationTextTrainingOverview.visibility = View.VISIBLE
            dateFieldTrainingOverview.visibility = View.VISIBLE
            categoriesTrainingOverview.visibility = View.VISIBLE
            trainingExerciseList.visibility = View.VISIBLE
        }
    }

    //delete dialog
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDeleteConfirmationDialog(training: Training) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this training?")
        builder.setPositiveButton("Delete") { _, _ ->
            // User clicked the "Delete" button, so delete the training.
            TrainingRepository.getInstance().deleteTraining(training.name)
            val trainingOverviewViewModel = ViewModelProvider(this).get(TrainingOverviewViewModel::class.java)
            trainingOverviewViewModel.refreshTrainingList()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // User clicked the "Cancel" button, so dismiss the dialog
            // and continue editing the training.
            dialog?.dismiss()
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}