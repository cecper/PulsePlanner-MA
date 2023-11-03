package com.example.pulseplanner.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.pulseplanner.R
import com.example.pulseplanner.model.Category

class CategoryAdapter(
    context: Context,
    private var categoryList: MutableList<Category>,
    private val onDeleteClickListener: (Category) -> Unit
) : ArrayAdapter<Category>(context, R.layout.category_item, categoryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.category_item, parent, false)

        val item = getItem(position)
        val textView = view.findViewById<TextView>(R.id.categoryNameTextView)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        textView.text = item?.categoryName
        deleteButton.setOnClickListener {
            onDeleteClickListener(item!!)
        }

        return view
    }

    // Update the category list when it changes.
    fun updateCategoryList(newCategoryList: List<Category>) {
        clear()
        addAll(newCategoryList)
        notifyDataSetChanged()
    }
}



