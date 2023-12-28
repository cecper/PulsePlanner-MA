package com.example.pulseplanner.ui.home

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.FragmentHomeBinding
import com.example.pulseplanner.model.Training
import com.example.pulseplanner.ui.trainingoverview.TrainingExerciseListAdapter
import com.example.pulseplanner.ui.trainingoverview.TrainingOverviewAdapter
import com.example.pulseplanner.ui.trainingoverview.TrainingOverviewViewModel
import com.google.android.material.datepicker.DayViewDecorator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    var selectedDate: LocalDate? = null

    private val _trainingList = MutableLiveData<List<Training>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val trainingOverviewViewModel = ViewModelProvider(this).get(TrainingOverviewViewModel::class.java)

        dateTV = root.findViewById<TextView>(R.id.idTVDate)
        calendarView = root.findViewById<CalendarView>(R.id.calendarView)
        val trainingListview=root.findViewById<android.widget.ListView>(R.id.trainingOverviewListView)
        val dateNow= LocalDate.now()
        dateTV.setText(dateNow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))

        //calendarView = refreshCalendarView(calendarView)

        // single training
        val goBackButton = root.findViewById<Button>(com.example.pulseplanner.R.id.goBackButton)
        val trainingNameTextView = root.findViewById<TextView>(com.example.pulseplanner.R.id.trainingOverviewNameTextView)
        val durationTextTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.durationTextTrainingOverview)
        val dateTimeFieldTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.dateTimeFieldTrainingOverview)
        val categoriesTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.categoriesTrainingOverview)
        val trainingExerciseList = root.findViewById<ListView>(com.example.pulseplanner.R.id.trainingExerciseList)
        setAllTrainingMode(true)

        // set the background color of 15/11/2023 to red
        val dayView = getDayView(calendarView, 15, 11, 2023)
        dayView?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))

        //make for loop for all dates in calendarview
        //set the background color of the date to red if there is a training on that date

        for (training in TrainingRepository.getInstance().getTrainings()) {
            val day = training.dateTime.dayOfMonth
            val month = training.dateTime.monthValue - 1 // months are zero-indexed
            val year = training.dateTime.year

            val dayView = getDayView(calendarView, day, month, year)
            dayView?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }


        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->

                    val Date = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    selectedDate=  LocalDate.of(year, month+1, dayOfMonth)
                    _trainingList.value = TrainingRepository.getInstance().getTrainingsOfDate(
                        selectedDate!!
                    )
                    val adapter = TrainingOverviewAdapter(
                        requireContext(),
                        _trainingList.value!!.toMutableList()
                    )

                    adapter.setOnDeletedTrainingListener { training ->
                        showDeleteConfirmationDialog(training)
                    }

                    adapter.setOnViewTrainingListener { training ->
                        //adapter2.updateTrainingExerciseList(training.exercises) todo
                        println("training name: " + training.name)
                        trainingNameTextView.text = training.name
                        // display duration as ..h ..min if duration is more than 60 minutes otherwise display only minutes
                        val durationMinutes = training.getDurationMinutes()
                        val durationHours = durationMinutes.div(60)
                        val durationMinutesRemaining = durationMinutes.rem(60)
                        durationTextTrainingOverview.text =
                            "Tot: ${if (durationHours != 0) "${durationHours}h " else ""}${durationMinutesRemaining}min"
                        // display dateTime in format dd/MM/yyyy HHh mmm
                        dateTimeFieldTrainingOverview.text = training.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        val categoryNames = training.getCategories().map { it.categoryName }.joinToString(", ")
                        categoriesTrainingOverview.text = categoryNames
                        setAllTrainingMode(false)
                    }

                    trainingListview.adapter = adapter

                    dateTV.setText(Date)
                })

        _trainingList.value = TrainingRepository.getInstance().getTrainingsOfDate(
            LocalDate.now()
        )

        val adapter2 = TrainingExerciseListAdapter(
            requireContext(),
            mutableListOf()
        )
        trainingExerciseList.adapter = adapter2

        val adapter = TrainingOverviewAdapter(
            requireContext(),
            _trainingList.value!!.toMutableList()
        )

        adapter.setOnDeletedTrainingListener { training ->
            showDeleteConfirmationDialog(training)
        }



        adapter.setOnViewTrainingListener { training ->
            adapter2.updateTrainingExerciseList(training.exercises)
            println("training name: " + training.name)
            trainingNameTextView.text = training.name
            // display duration as ..h ..min if duration is more than 60 minutes otherwise display only minutes
            val durationMinutes = training.getDurationMinutes()
            val durationHours = durationMinutes.div(60)
            val durationMinutesRemaining = durationMinutes.rem(60)
            durationTextTrainingOverview.text =
                "Tot: ${if (durationHours != 0) "${durationHours}h " else ""}${durationMinutesRemaining}min"
            // display dateTime in format dd/MM/yyyy HHh mmm
            dateTimeFieldTrainingOverview.text = training.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            val categoryNames = training.getCategories().map { it.categoryName }.joinToString(", ")
            categoriesTrainingOverview.text = categoryNames
            setAllTrainingMode(false)
        }
        trainingListview.adapter = adapter

        trainingOverviewViewModel.trainingList.observe(viewLifecycleOwner) {
            //adapter.updateTrainingList(it.toMutableList())
        }
        //calendar shit


        goBackButton.setOnClickListener {
            setAllTrainingMode(true)
        }

        return root
    }

//    //return the calendarview with colors
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun refreshCalendarView(calendarView : CalendarView) : CalendarView {
//        val trainingList = TrainingRepository.getInstance().getTrainings()
//        val dateColorMap = mutableMapOf<LocalDate, Int>()
//
//        for (training in trainingList) {
//            dateColorMap.put(training.dateTime.toLocalDate(), R.style.DateTextAppearanceColor1)
//        }
//
//        // Set a background color for specific dates
//        calendarView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onPreDraw(): Boolean {
//                calendarView.viewTreeObserver.removeOnPreDrawListener(this)
//                for ((date, colorResId) in dateColorMap) {
//                    val day = date.dayOfMonth
//                    val month = date.monthValue - 1 // months are zero-indexed
//                    val year = date.year
//
//                    val dayView = getDayView(calendarView, day, month, year)
//                    dayView?.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
//                }
//                return true
//            }
//        })
//
//        return calendarView;
//    }


    // Helper function to get the day view for a specific date
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDayView(calendarView: CalendarView, day: Int, month: Int, year: Int): View? {
        val firstDayOfMonth = LocalDate.of(year, month + 1, 1)
        val dayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Convert to 0-6 range (Sunday-Saturday)
        val offset = (day + dayOfWeek - 2) % 7 // Calculate the offset for the specific day

        // Calculate the position of the day view in the CalendarView
        val childIndex = day + offset
        val weekIndex = childIndex / 7

        // Get the week view (TableRow) at the calculated position
        val weekView = (calendarView.getChildAt(0) as? LinearLayout)?.getChildAt(weekIndex) as? TableRow

        // Get the day view (TextView) at the calculated position
        return weekView?.getChildAt(childIndex % 7)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDeleteConfirmationDialog(training: Training) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this training?")
        builder.setPositiveButton("Delete") { _, _ ->
            // User clicked the "Delete" button, so delete the training.
            TrainingRepository.getInstance().deleteTraining(training.name)
            val trainingOverviewViewModel = ViewModelProvider(this).get(TrainingOverviewViewModel::class.java)
            trainingOverviewViewModel.refreshTrainingList()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // User clicked the "Cancel" button, so dismiss the dialog
            // and continue editing the training.
            dialog?.dismiss()
        }
        builder.create().show()
    }

    private fun setAllTrainingMode(boolean: Boolean) {
        val root: View = binding.root

        // all trainings
        val trainingList = root.findViewById<android.widget.ListView>(com.example.pulseplanner.R.id.trainingOverviewListView)

        // single training
        val goBackButton = root.findViewById<Button>(com.example.pulseplanner.R.id.goBackButton)
        val trainingNameTextView = root.findViewById<TextView>(com.example.pulseplanner.R.id.trainingOverviewNameTextView)
        val durationTextTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.durationTextTrainingOverview)
        val dateFieldTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.dateTimeFieldTrainingOverview)
        val categoriesTrainingOverview = root.findViewById<TextView>(com.example.pulseplanner.R.id.categoriesTrainingOverview)
        val trainingExerciseList = root.findViewById<ListView>(com.example.pulseplanner.R.id.trainingExerciseList)

        if (boolean) {
            trainingList.visibility = View.VISIBLE
            goBackButton.visibility = View.GONE
            trainingNameTextView.visibility = View.GONE
            durationTextTrainingOverview.visibility = View.GONE
            dateFieldTrainingOverview.visibility = View.GONE
            categoriesTrainingOverview.visibility = View.GONE
            trainingExerciseList.visibility = View.GONE
            dateTV.visibility=View.VISIBLE
            calendarView.visibility=View.VISIBLE

        } else {
            trainingList.visibility = View.GONE
            goBackButton.visibility = View.VISIBLE
            trainingNameTextView.visibility = View.VISIBLE
            durationTextTrainingOverview.visibility = View.VISIBLE
            dateFieldTrainingOverview.visibility = View.VISIBLE
            categoriesTrainingOverview.visibility = View.VISIBLE
            trainingExerciseList.visibility = View.VISIBLE
            dateTV.visibility=View.GONE
            calendarView.visibility=View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}