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
    exercises: List<Exercise>,
    private val onClickShowDeleteDialog: (Exercise) -> Unit
) : ArrayAdapter<Exercise>(context, 0, exercises) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false)
        }

        val exercise = getItem(position)

        val exerciseNameTextView = listItemView?.findViewById<TextView>(R.id.exerciseNameTextView)
        exerciseNameTextView?.text = exercise?.name

        val deleteButton = listItemView?.findViewById<TextView>(R.id.deleteButton)
        deleteButton?.setOnClickListener {
            onClickShowDeleteDialog(exercise!!)
        }

        val exerciseDescriptionTextView = listItemView?.findViewById<TextView>(R.id.exerciseDescriptionTextView)
        exerciseDescriptionTextView?.text = exercise?.description

        val exerciseCategoriesTextView = listItemView?.findViewById<TextView>(R.id.exerciseCategoriesTextView)

        // Construct a string to display exercise categories
        val categoryNames = exercise?.categories?.map { it.categoryName }?.joinToString(", ")
        exerciseCategoriesTextView?.text = categoryNames

        return listItemView!!
    }

    fun updateExerciseList(newExerciseList: List<Exercise>) {
        clear() // Clear the existing items in the adapter
        addAll(newExerciseList) // Add the updated items to the adapter
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
