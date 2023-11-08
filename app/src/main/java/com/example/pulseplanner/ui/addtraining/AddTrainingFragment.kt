package com.example.pulseplanner.ui.addtraining

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentAddTrainingBinding
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

        val nameField = binding.root.findViewById<EditText>(R.id.nameField)
        val dateField = binding.root.findViewById<EditText>(R.id.dateField)
        val timeField = binding.root.findViewById<EditText>(R.id.timeField)
        val saveButton = binding.root.findViewById<Button>(R.id.saveButton)

        // Show a date picker when the date field is clicked
        dateField.setOnClickListener {
            showDatePicker(dateField)
        }

        // get fields from the layout
        val root: View = binding.root

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

}