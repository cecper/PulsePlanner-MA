package com.example.pulseplanner.ui.exercise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.lifecycle.Observer

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.databinding.FragmentExerciseBinding
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.model.Exercise
import com.example.pulseplanner.util.TextUtils


class ExerciseFragment : Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var exerciseAdapter: ExerciseAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        val exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

        val root: View = binding.root
        val listView = root.findViewById<ListView>(com.example.pulseplanner.R.id.categoryListView)
        val searchField = root.findViewById<EditText>(com.example.pulseplanner.R.id.searchField)
        val addCategoryButton = root.findViewById<Button>(com.example.pulseplanner.R.id.addCategoryButton)
        val selectedCategories = root.findViewById<ListView>(com.example.pulseplanner.R.id.addedCategories)

        val exerciseName= root.findViewById<EditText>(com.example.pulseplanner.R.id.exerciseNameEditText)
        val exerciseDescription = root.findViewById<EditText>(com.example.pulseplanner.R.id.exerciseDescriptionEditText)
        val createExercise = root.findViewById<Button>(com.example.pulseplanner.R.id.submitExerciseButton)


        // Initialize the adapter with empty data
        exerciseAdapter = ExerciseAdapter(requireContext(), mutableListOf(), mutableListOf()) { category ->
            selectCategory(category)
        }

        listView.adapter = exerciseAdapter

        val selectedCategoriesAdapter = ExerciseAdapter(requireContext(), mutableListOf(), mutableListOf(),true) { category ->
            deselectCategory(category)
        }

        selectedCategories.adapter = selectedCategoriesAdapter

        exerciseViewModel.selectedCategoriesList.observe(
            viewLifecycleOwner,
            Observer { newCategoryList ->
                // Update the UI with the new data for selectedCategories
                selectedCategoriesAdapter.updateCategoryList(newCategoryList)
                exerciseAdapter.updateCategoryList(exerciseViewModel.categoryList.value ?: emptyList(), newCategoryList)
            }
        )

        exerciseViewModel.categoryList.observe(viewLifecycleOwner, Observer { newCategoryList ->
            exerciseAdapter.updateCategoryList(newCategoryList)
        })

        addCategoryButton.setOnClickListener {
            showAddCategoryDialog(searchField.text.toString())
        }

        createExercise.setOnClickListener{
            addExercise(Exercise(exerciseName.text.toString(), exerciseViewModel.selectedCategoriesList.value ?: emptyList(), exerciseDescription.text.toString()))
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString()
                val categoryList = exerciseViewModel.categoryList.value ?: emptyList()
                val sortedList = categoryList.sortedByDescending { category ->
                    TextUtils.getSimilarity(category.categoryName.toString(), newText)
                }
                exerciseAdapter.updateCategoryList(sortedList, exerciseViewModel.selectedCategoriesList.value ?: emptyList())
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        return root
    }

    private fun showAddCategoryDialog(categoryText: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Category")
        builder.setMessage("Are you sure you want to add $categoryText?")

        builder.setPositiveButton("Add") { dialog, which ->
            addCategory(categoryText)
        }

        builder.setNegativeButton("Cancel") { dialog, which -> /* nothing */ }

        builder.create().show()
    }

    private fun addCategory(categoryName: String) {
        val exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        try {
            exerciseViewModel.addCategory(categoryName)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectCategory(category: Category) {
        // ViewModel logic for selecting categories
        ViewModelProvider(this)[ExerciseViewModel::class.java].addToSelectedCategories(category)
        val exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        exerciseAdapter.updateSelectedCategoryList(exerciseViewModel.selectedCategoriesList.value ?: emptyList())
    }

    private fun deselectCategory(category: Category) {
        ViewModelProvider(this)[ExerciseViewModel::class.java].removeFromSelectedCategories(category)
        val exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        exerciseAdapter.updateSelectedCategoryList(exerciseViewModel.selectedCategoriesList.value ?: emptyList())
    }

    private fun addExercise(exercise: Exercise) {
        val exerciseViewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

        // check name not empty
        if (exercise.name.isEmpty()) {
            Toast.makeText(requireContext(), "Exercise name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            exerciseViewModel.addExercise(exercise)
            Toast.makeText(requireContext(), "Exercise added", Toast.LENGTH_SHORT).show()

            // reset fields
            binding.exerciseNameEditText.setText("")
            binding.exerciseDescriptionEditText.setText("")
            exerciseViewModel.selectedCategoriesList.value = emptyList()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }


}
