package com.example.pulseplanner.ui.trainingoverview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.FragmentTrainingOverviewBinding
import com.example.pulseplanner.model.Training

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

        val trainingList = root.findViewById<android.widget.ListView>(com.example.pulseplanner.R.id.trainingOverviewListView)

        println("TrainingOverviewFragment.onCreateView")

        trainingOverviewViewModel.refreshTrainingList()

        val adapter = TrainingOverviewAdapter(
            requireContext(),
            trainingOverviewViewModel.trainingList.value!!.toMutableList()
        ) { training ->
            showDeleteConfirmationDialog(training)
        }
        trainingList.adapter = adapter

        trainingOverviewViewModel.trainingList.observe(viewLifecycleOwner) {
            adapter.updateTrainingList(it.toMutableList())
        }



        return root
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