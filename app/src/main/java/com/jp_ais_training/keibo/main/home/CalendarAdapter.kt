package com.jp_ais_training.keibo.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jp_ais_training.keibo.databinding.CalendarBinding
import com.jp_ais_training.keibo.databinding.CalendarItemBinding

class CalendarAdapter(private val itemList: ArrayList<CalendarItem>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.context))


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // 캘린더 뷰에 작성될 홀더 바인딩
    inner class ViewHolder(private val binding: CalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarItem: CalendarItem) {
            if (calendarItem.date != 0)
                binding.calendarItemDate.text = calendarItem.date.toString()
            else
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

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}