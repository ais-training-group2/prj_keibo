package com.jp_ais_training.keibo.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.jp_ais_training.keibo.databinding.FragmentHomeBinding
import com.jp_ais_training.keibo.main.Const
import com.jp_ais_training.main.sharedPreferences.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val TAG = this::class.java.simpleName.toString()
    private lateinit var app: MyApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        app = requireActivity().application as MyApplication

        CoroutineScope(Dispatchers.IO).launch {
            setCalendar()
        }

        return binding.root
    }

    private fun setCalendar() {
        val numberOfWeek = 7

        val dataSet = arrayListOf<CalendarItem>()

        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.MONTH,0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")     // 년월일 날짜 포멧
        val dateNum = calendar.getMaximum(Calendar.DAY_OF_MONTH)    // 당월의 마지막 날
        val datePadding = calendar.get(Calendar.DAY_OF_WEEK)        // 당월 1일의 요일
        val yearMonthFormat = SimpleDateFormat("yyyy-MM")    // 년월 날짜 포멧
        val yearMonth = yearMonthFormat.format(calendar.time)

        val expenseItemList = app.db.loadDaySumEI(yearMonth)
        val incomeItemList = app.db.loadDaySumII(yearMonth)

        Log.d(TAG, "date: " + dateFormat.format(calendar.time))
        Log.d(TAG, "dateNum: $dateNum")
        Log.d(TAG, "datePadding: $datePadding")

        // 1일이 해당하는 요일까지 패딩 (1일이 수요일이라면 일, 월, 화요일이 패딩)
        for (i in 1 until datePadding) {
            dataSet.add(CalendarItem(0, Const.NULL, 0, 0))
        }
        // 실제 캘린더에 들어갈 데이터 작성
        for (i in 1..dateNum) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val date = dateFormat.format(calendar.time)
            val incomeItem = incomeItemList.firstOrNull { it.date == date }
            val income = if (incomeItem != null) incomeItem.price!! else 0
            val expenseItem = expenseItemList.firstOrNull { it.date == date }
            val expense = if (expenseItem != null) expenseItem.price!! else 0
            dataSet.add(
                CalendarItem(
                    i,
                    date,
                    income,
                    expense
                )
            )
        }
        binding.homeCalendar.calendar.layoutManager = GridLayoutManager(context, numberOfWeek)
        binding.homeCalendar.calendar.adapter = CalendarAdapter(dataSet, context)
    }
}