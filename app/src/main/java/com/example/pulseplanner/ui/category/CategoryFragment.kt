package com.example.pulseplanner.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView = root.findViewById<ListView>(R.id.categoryListView)

        // You can set an adapter for the ListView to display the data.
        // Create an ArrayAdapter and set it for the ListView.
        val categoryList = categoryViewModel.categoryList.value ?: emptyList()
        val adapter = CategoryAdapter(requireContext(), categoryList)
        listView.adapter = adapter





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


