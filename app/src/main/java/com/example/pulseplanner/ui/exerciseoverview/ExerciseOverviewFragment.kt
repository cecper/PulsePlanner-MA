package com.example.pulseplanner.ui.exerciseoverview

import ExerciseOverviewAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentExerciseOverviewBinding
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

        val adapter = ExerciseOverviewAdapter(requireContext(), exerciseOverviewViewModel.exerciseList.value!!)
        listView.adapter = adapter

        exerciseOverviewViewModel.refreshExerciseList()

        exerciseOverviewViewModel.exerciseList.observe(viewLifecycleOwner, Observer { newExerciseList ->
            // Update the UI with the new data when exerciseList changes
            val adapter = listView.adapter as ExerciseOverviewAdapter
            println("Exercise list changed: $newExerciseList")
            //print all exercises
            for (exercise in newExerciseList) {
                println("Exercise: ${exercise.name} ${exercise.description} ${exercise.categories}")
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}