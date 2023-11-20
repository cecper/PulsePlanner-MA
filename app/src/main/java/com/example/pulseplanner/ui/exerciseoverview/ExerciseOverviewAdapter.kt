import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pulseplanner.R
import com.example.pulseplanner.model.Exercise

class ExerciseOverviewAdapter(
    context: Context,
    exercises: MutableList<Exercise>,
    private val onClickShowDeleteDialog: (Exercise) -> Unit
) : ArrayAdapter<Exercise>(context, 0, exercises) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false)
        }

        val exercise = getItem(position)

        val exerciseNameTextView = listItemView?.findViewById<TextView>(R.id.exerciseNameTextView)
        val deleteButton = listItemView?.findViewById<TextView>(R.id.deleteButton)
        val exerciseDescriptionTextView = listItemView?.findViewById<TextView>(R.id.exerciseDescriptionTextView)
        val exerciseCategoriesTextView = listItemView?.findViewById<TextView>(R.id.exerciseCategoriesTextView)

        exerciseNameTextView?.text = exercise?.name
        exerciseDescriptionTextView?.text = "description: "+exercise?.description
        val categoryNames = exercise?.categories?.map { it.categoryName }?.joinToString(", ")
        exerciseCategoriesTextView?.text = "Categories: "+categoryNames

        deleteButton?.setOnClickListener {
            onClickShowDeleteDialog(exercise!!)
        }

        return listItemView!!
    }

    fun updateExerciseList(newExerciseList: List<Exercise>) {
        clear() // Clear the existing items in the adapter
        addAll(newExerciseList) // Add the updated items to the adapter
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
