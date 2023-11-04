package com.example.pulseplanner.ui.exercise

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.pulseplanner.R
import com.example.pulseplanner.model.Category

class ExerciseAdapter(
    private val context: Context,
    private var categoryList: MutableList<Category>,
    private var selectedCategoryList: MutableList<Category>,
    private val onAddToSelectedClickListener: (Category) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int {
        // Return the count of the categoryList
        return categoryList.size
    }

    override fun getItem(position: Int): Category {
        // Return the category from categoryList
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.category_item_exercise, parent, false)

        val item = getItem(position)
        val textView = view.findViewById<TextView>(R.id.categoryNameTextView)

        textView.text = item.categoryName

        val addToSelectedButton = view.findViewById<Button>(R.id.selectButton)

        // Set the click listener for adding to selected categories
        addToSelectedButton.setOnClickListener {
            onAddToSelectedClickListener(item)
            // Instead of directly modifying the selectedCategoryList, notify the selectedCategoriesAdapter
            updateSelectedCategoryList(selectedCategoryList)
        }


        return view
    }

    // Update the categoryList when it changes
    fun updateCategoryList(newCategoryList: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newCategoryList)
        notifyDataSetChanged()
    }

    // Update the selectedCategoryList when it changes
    fun updateSelectedCategoryList(newSelectedCategoryList: List<Category>) {
        selectedCategoryList.clear()
        selectedCategoryList.addAll(newSelectedCategoryList)
        notifyDataSetChanged()
    }

    fun getSelectedCategoryList(): List<Category> {
        return selectedCategoryList
    }
}

