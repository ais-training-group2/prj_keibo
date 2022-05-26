package com.jp_ais_training.keibo.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jp_ais_training.keibo.databinding.CalendarItemBinding
import com.jp_ais_training.keibo.main.Const

class CalendarAdapter(private val itemList: ArrayList<CalendarItem>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    val TAG = "View Holder"
    // 캘린더 뷰에 작성될 홀더
    inner class ViewHolder(private val binding: CalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            // 아이템 클릭 리스너 작성
            binding.calendarItem.setOnClickListener {
                Log.d(TAG, itemList[bindingAdapterPosition].date)
            }
            
        }

        // 뷰 바인딩
        fun bind(calendarItem: CalendarItem) {
            if (calendarItem.date != Const.NULL) {
                var day = calendarItem.date.substring(8, 10)
                day = day.toInt().toString()
                binding.calendarItemDate.text = day
            } else
                binding.calendarItemDate.text = ""
            if (calendarItem.income != 0)
                binding.calendarItemIncome.text = "+" + calendarItem.income.toString()
            else
                binding.calendarItemIncome.text = ""
            if (calendarItem.expense != 0)
                binding.calendarItemExpense.text = "-" + calendarItem.expense.toString()
            else
                binding.calendarItemExpense.text = ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.context))


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}