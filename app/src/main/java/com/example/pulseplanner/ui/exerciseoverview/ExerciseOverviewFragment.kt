package com.example.pulseplanner.ui.exerciseoverview

import ExerciseOverviewAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentExerciseOverviewBinding
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.Exercise
import com.example.pulseplanner.ui.category.CategoryAdapter
import com.example.pulseplanner.util.TextUtils

class ExerciseOverviewFragment : Fragment() {

    private var _binding: FragmentExerciseOverviewBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseOverviewBinding.inflate(inflater, container, false)
        val exerciseOverviewViewModel = ViewModelProvider(this).get(ExerciseOverviewViewModel::class.java)

        println("ExerciseOverviewFragment.onCreateView")


        // get fields from the layout
        val root: View = binding.root
        val listView = root.findViewById<ListView>(R.id.exerciseOverview)
        val searchField = root.findViewById<EditText>(R.id.exerciseSearchField)

        val exerciseList = exerciseOverviewViewModel.exerciseList.value ?: emptyList()
        val adapter = ExerciseOverviewAdapter(requireContext(), exerciseList) { exercise ->
            showDeleteConfirmationDialog(exercise)
        }
        listView.adapter = adapter

        exerciseOverviewViewModel.refreshExerciseList()

        exerciseOverviewViewModel.exerciseList.observe(viewLifecycleOwner, Observer { newExerciseList ->
            // Update the UI with the new data when exerciseList changes
            adapter.updateExerciseList(newExerciseList)
        })

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changed.
                exerciseOverviewViewModel.refreshExerciseList()
                println("onTextChanged: $s")
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        })

        return root
    }

    private fun showDeleteConfirmationDialog(exercise: Exercise) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Exercise")
        builder.setMessage("Are you sure you want to delete ${exercise.name}?")

        //Pressed confirm
        builder.setPositiveButton("Delete") { dialog, which ->
            deleteExercise(exercise)
        }

        //Pressed cancel
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do nothing, cancel the deletion.
        }

        builder.create().show()
    }

    private fun deleteExercise(exercise: Exercise) {
        val exerciseOverviewViewModel = ViewModelProvider(this).get(ExerciseOverviewViewModel::class.java)
        exerciseOverviewViewModel.deleteExercise(exercise)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}