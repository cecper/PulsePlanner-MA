package com.example.pulseplanner.ui.category

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.databinding.FragmentCategoryBinding
import com.example.pulseplanner.model.Category
import com.example.pulseplanner.util.TextUtils

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val textUtils = TextUtils()




    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView = root.findViewById<ListView>(R.id.categoryListView)
        val searchField = root.findViewById<EditText>(R.id.searchField)

        val categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        // auto update the UI when categoryList changes
        categoryViewModel.categoryList.observe(viewLifecycleOwner, Observer { newCategoryList ->
            // Update the UI with the new data when categoryList changes
            val adapter = listView.adapter as CategoryAdapter
            adapter.updateCategoryList(newCategoryList)
        })



        // Add a TextWatcher to listen for text changes
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called as the text is being changed.
                val newText = s.toString()

                // sort the category list based on the similarity of the category name to the search text
                val categoryList = categoryViewModel.categoryList.value ?: emptyList()
                val sortedList = categoryList.sortedByDescending { category ->
                    textUtils.getSimilarity(category.categoryName.toString(), newText)
                }

                categoryViewModel.updateCategoryList(sortedList)
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        })


        val categoryList = categoryViewModel.categoryList.value ?: emptyList()
        val adapter = CategoryAdapter(requireContext(), categoryList.toMutableList()) { category ->
            showDeleteConfirmationDialog(category)
        }
        listView.adapter = adapter

        return root
    }

    //Show a confirmation delete
    private fun showDeleteConfirmationDialog(category: Category) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Category")
        builder.setMessage("Are you sure you want to delete ${category.categoryName}?")

        //Pressed confirm
        builder.setPositiveButton("Delete") { dialog, which ->
            deleteCategory(category)
        }

        //Pressed cancel
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do nothing, cancel the deletion.
        }

        builder.create().show()
    }

    private fun deleteCategory(category: Category) {
        println("CategoryFragment: deleteCategory: " + category.categoryName)
        val categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.deleteCategory(category)
    }

}


