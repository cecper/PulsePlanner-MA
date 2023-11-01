package com.example.pulseplanner.ui.exercise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment() {

    //put the text Exercise here
    private var _binding: FragmentExerciseBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exerciseViewModel =
            ViewModelProvider(this).get(ExerciseViewModel::class.java)

        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.exerciseText

        exerciseViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }



}