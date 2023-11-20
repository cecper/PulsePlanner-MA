package com.example.pulseplanner.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.pulseplanner.R
import com.example.pulseplanner.Repositories.TrainingRepository
import com.example.pulseplanner.databinding.FragmentHomeBinding
import com.example.pulseplanner.model.Training
import com.example.pulseplanner.ui.trainingoverview.TrainingOverviewAdapter
import com.example.pulseplanner.ui.trainingoverview.TrainingOverviewViewModel
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
    lateinit var trainingListview:ListView
    var selectedDate: LocalDate? = null

    private val _trainingList = MutableLiveData<List<Training>>()

    val trainingList: MutableLiveData<List<Training>> = _trainingList
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
        val textView: TextView = binding.textHome

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        dateTV = root.findViewById(R.id.idTVDate)
        calendarView = root.findViewById(R.id.calendarView)
        trainingListview=root.findViewById<android.widget.ListView>(R.id.trainingOverviewListView)

        // on below line we are adding set on
        // date change listener for calendar view.
        _trainingList.value = TrainingRepository.getInstance().getTrainingsOfDate(
            LocalDate.now()
        )
        val adapter = TrainingOverviewAdapter(
            requireContext(),
            _trainingList.value!!.toMutableList()
        )

        trainingListview.adapter = adapter

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


                    trainingListview.adapter = adapter

                    dateTV.setText(Date)
                })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}