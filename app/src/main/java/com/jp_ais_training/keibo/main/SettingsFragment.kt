package com.jp_ais_training.keibo.main

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jp_ais_training.keibo.R
import com.jp_ais_training.keibo.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val TAG = this::class.java.simpleName.toString()
    private lateinit var binding:FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences   // 이전 스위치 상태 저장 객체

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        initSwitchValue()
        setClickEvent()

        return binding.root
    }

    // 스위치 상태 초기화
    private fun initSwitchValue() {
        // 이전 스위치 상태 확인 및 대입
        sharedPreferences = requireContext().getSharedPreferences(Const.NOTI_KEY, MODE_PRIVATE)
        val alarm1isRunning = sharedPreferences.getBoolean(Const.FIX_EXPENSE_NOTI_KEY, false)
        val alarm2isRunning = sharedPreferences.getBoolean(Const.KINYU_NOTI_KEY, false)
        val alarm3isRunning = sharedPreferences.getBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, false)
        binding.swiAll.isChecked = alarm1isRunning && alarm2isRunning && alarm3isRunning
        binding.swiAlarm1.isChecked = alarm1isRunning
        binding.swiAlarm2.isChecked = alarm2isRunning
        binding.swiAlarm3.isChecked = alarm3isRunning
    }

    // 화면 클릭 이벤트 설정
    private fun setClickEvent() {

        binding.swiAll.setOnClickListener {
            val isChecked = binding.swiAll.isChecked
            binding.swiAlarm1.isChecked = isChecked
            binding.swiAlarm2.isChecked = isChecked
            binding.swiAlarm3.isChecked = isChecked

            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.FIX_EXPENSE_NOTI_KEY, isChecked)
            editor.putBoolean(Const.KINYU_NOTI_KEY, isChecked)
            editor.putBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()
        }

        binding.swiAlarm1.setOnClickListener {
            val isChecked = binding.swiAlarm1.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.FIX_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = isChecked && binding.swiAlarm2.isChecked && binding.swiAlarm3.isChecked

            if (isChecked) {
                setAlarm(Const.FIX_EXPENSE_NOTI_KEY)
            } else {
                closeAlarm(Const.FIX_EXPENSE_NOTI_KEY)
            }
        }

        binding.swiAlarm2.setOnClickListener {
            val isChecked = binding.swiAlarm2.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.KINYU_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = binding.swiAlarm1.isChecked && isChecked  && binding.swiAlarm3.isChecked

            if (isChecked) {
                setAlarm(Const.KINYU_NOTI_KEY)
            } else {
                closeAlarm(Const.KINYU_NOTI_KEY)
            }
        }

        binding.swiAlarm3.setOnClickListener {
            val isChecked = binding.swiAlarm3.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = binding.swiAlarm1.isChecked && binding.swiAlarm2.isChecked  && isChecked

            if (isChecked) {
                setAlarm(Const.COMPARISON_EXPENSE_NOTI_KEY)
            } else {
                closeAlarm(Const.COMPARISON_EXPENSE_NOTI_KEY)
            }
        }

    }

    private fun setAlarm(flag: String) {
        when(flag) {
            Const.FIX_EXPENSE_NOTI_KEY -> Log.d(TAG, "1번 알람 설정")
            Const.KINYU_NOTI_KEY -> Log.d(TAG, "2번 알람 설정")
            Const.COMPARISON_EXPENSE_NOTI_KEY -> Log.d(TAG, "3번 알람 설정")
        }
    }

    private fun closeAlarm(flag: String) {
        when(flag) {
            Const.FIX_EXPENSE_NOTI_KEY -> Log.d(TAG, "1번 알람 취소")
            Const.KINYU_NOTI_KEY -> Log.d(TAG, "2번 알람 취소")
            Const.COMPARISON_EXPENSE_NOTI_KEY -> Log.d(TAG, "3번 알람 취소")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}