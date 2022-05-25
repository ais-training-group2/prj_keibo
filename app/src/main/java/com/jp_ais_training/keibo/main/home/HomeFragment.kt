package com.jp_ais_training.keibo.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.jp_ais_training.keibo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        val numberOfWeek = 7

        var dataSet = arrayListOf<CalendarItem>()

        var dateNum = 30
        var datePadding = 5

        for (i in 0..datePadding) {
            dataSet.add(CalendarItem(0,0,0,0))
        }
        for (i in 0..dateNum) {
            dataSet.add(CalendarItem(i,i+1,1000,1000))
        }
        binding.homeCalendar.calendar.layoutManager=GridLayoutManager(context,numberOfWeek)
        binding.homeCalendar.calendar.adapter = CalendarAdapter(dataSet)
        binding.homeCalendar.calendar.adapter
        return binding.root
    }
}