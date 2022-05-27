package com.jp_ais_training.keibo.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jp_ais_training.keibo.databinding.FragmentHomeBinding
import com.jp_ais_training.keibo.main.Const
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val TAG = this::class.java.simpleName.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        val numberOfWeek = 7

        val dataSet = arrayListOf<CalendarItem>()

        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.MONTH,0)
        calendar.set(Calendar.DAY_OF_MONTH,1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateNum = calendar.getMaximum(Calendar.DAY_OF_MONTH)
        val datePadding = calendar.get(Calendar.DAY_OF_WEEK)
        Log.d(TAG, "date: " + dateFormat.format(calendar.time))
        Log.d(TAG, "dateNum: $dateNum")
        Log.d(TAG, "datePadding: $datePadding")

        for (i in 1 until datePadding) {
            dataSet.add(CalendarItem(0, Const.NULL, 0, 0))
        }
        for (i in 1..dateNum) {
            calendar.set(Calendar.DAY_OF_MONTH,i)
            dataSet.add(CalendarItem(i, dateFormat.format(calendar.time), 1000, 1000))
        }
        binding.homeCalendar.calendar.layoutManager = GridLayoutManager(context, numberOfWeek)
        binding.homeCalendar.calendar.adapter = CalendarAdapter(dataSet, context)
        return binding.root
    }
}