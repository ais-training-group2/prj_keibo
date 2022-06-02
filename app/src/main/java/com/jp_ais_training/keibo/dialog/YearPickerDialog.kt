package com.jp_ais_training.keibo.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.jp_ais_training.keibo.databinding.DialogYearPickerBinding
import java.util.*

class YearPickerDialog(context: Context): Dialog(context) {

    private lateinit var binding: DialogYearPickerBinding
    private lateinit var calendar: Calendar
    // 현재 연도로부터 과거 몇년까지 선택가능한가
    private val yearCountLimit = 5
    private var mCurrentYear = -1
    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

    interface ButtonClickListener {
        fun onClicked(year: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogYearPickerBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        initNumberPicker()
        setClickEvent()
        setNumberPickerValueChangeEvent()

    }

    // NumberPicker 초기화
    private fun initNumberPicker() {
        calendar = Calendar.getInstance()                   // 현재 날짜 객체 생성
        val currentYear = calendar.get(Calendar.YEAR)       // 현재 연
        binding.npYear.maxValue = currentYear               // 미래 데이터를 검색할 수 없기에 현재 년도를 최대값으로 설정
        binding.npYear.minValue = currentYear - yearCountLimit  // yearCountLimit을 이용해 (현재년도-N)년 ~ 현재년도
        binding.npYear.value = currentYear                      // default는 현재년도
    }

    // 클릭 이벤트 설정
    private fun setClickEvent() {
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            println(binding.npYear.value)
            onClickedListener.onClicked(binding.npYear.value.toString()+"年")
            this.dismiss()
        }
    }

    // NumberPicker의 ValueChange 이벤트 설정
    private fun setNumberPickerValueChangeEvent() {
        // 연도 NumberPicker ValueChange 이벤트 설정
        binding.npYear.setOnValueChangedListener { picker, oldVal, newVal ->
            mCurrentYear = newVal   // mCurrentYear 변수는 npMonth ValueChange Event에서 활용됨
        }
    }
}