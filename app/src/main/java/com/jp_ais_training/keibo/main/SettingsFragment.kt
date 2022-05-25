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

        Log.d(TAG, "onCreateView")

        initSwitchValue()
        setClickEvent()

        return binding.root
    }

    // 스위치 상태 초기화
    private fun initSwitchValue() {
        // 이전 스위치 상태 확인 및 대입
        sharedPreferences = requireContext().getSharedPreferences("Alarm", MODE_PRIVATE)
        val alarm1isRunning = sharedPreferences.getBoolean("Alarm1", false)
        val alarm2isRunning = sharedPreferences.getBoolean("Alarm2", false)
        val alarm3isRunning = sharedPreferences.getBoolean("Alarm3", false)
        binding.swiAll.isChecked = alarm1isRunning && alarm2isRunning && alarm3isRunning
        binding.swiAlarm1.isChecked = alarm1isRunning
        binding.swiAlarm2.isChecked = alarm2isRunning
        binding.swiAlarm3.isChecked = alarm3isRunning
    }

    // 화면 클릭 이벤트 설정
    private fun setClickEvent() {

        binding.swiAll.setOnClickListener {
            Log.d(TAG, "swiAll: ${binding.swiAll.isChecked}")
            val isChecked = binding.swiAll.isChecked
            binding.swiAlarm1.isChecked = isChecked
            binding.swiAlarm2.isChecked = isChecked
            binding.swiAlarm3.isChecked = isChecked

            val editor = sharedPreferences.edit()
            editor.putBoolean("Alarm1", isChecked)
            editor.putBoolean("Alarm2", isChecked)
            editor.putBoolean("Alarm3", isChecked)
            editor.commit()
        }

        binding.swiAlarm1.setOnClickListener {
            Log.d(TAG, "swiAlarm1: ${binding.swiAlarm1.isChecked}")
            val isChecked = binding.swiAlarm1.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean("Alarm1", isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = isChecked && binding.swiAlarm2.isChecked && binding.swiAlarm3.isChecked

            if (isChecked) {
                setAlarm(1)
            } else {
                closeAlarm(1)
            }
        }

        binding.swiAlarm2.setOnClickListener {
            Log.d(TAG, "swiAlarm2: ${binding.swiAlarm2.isChecked}")
            val isChecked = binding.swiAlarm2.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean("Alarm2", isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = binding.swiAlarm1.isChecked && isChecked  && binding.swiAlarm3.isChecked

            if (isChecked) {
                setAlarm(2)
            } else {
                closeAlarm(2)
            }
        }

        binding.swiAlarm3.setOnClickListener {
            Log.d(TAG, "swiAlarm3: ${binding.swiAlarm3.isChecked}")
            val isChecked = binding.swiAlarm3.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean("Alarm3", isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked = binding.swiAlarm1.isChecked && binding.swiAlarm2.isChecked  && isChecked

            if (isChecked) {
                setAlarm(3)
            } else {
                closeAlarm(3)
            }
        }

    }

    private fun setAlarm(flag: Int) {
        when(flag) {
            1 -> Log.d(TAG, "1번 알람 설정")
            2 -> Log.d(TAG, "2번 알람 설정")
            3 -> Log.d(TAG, "3번 알람 설정")
        }
    }

    private fun closeAlarm(flag: Int) {
        when(flag) {
            1 -> Log.d(TAG, "1번 알람 취소")
            2 -> Log.d(TAG, "2번 알람 취소")
            3 -> Log.d(TAG, "3번 알람 취소")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}