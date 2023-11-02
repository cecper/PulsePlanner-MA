package com.example.pulseplanner.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pulseplanner.model.Category

class CategoryAdapter(context: Context, categoryList: List<Category>) :
    ArrayAdapter<Category>(context, android.R.layout.simple_list_item_1, categoryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val item = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item?.categoryName // Display only the categoryName

        return view
    }
}
