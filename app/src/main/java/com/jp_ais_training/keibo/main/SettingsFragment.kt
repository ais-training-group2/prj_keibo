package com.jp_ais_training.keibo.main

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
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
        sharedPreferences = requireContext().getSharedPreferences(Const.NOTI_KEY,MODE_PRIVATE)
        val alarm1isRunning = sharedPreferences.getBoolean(Const.FIX_EXPENSE_NOTI_KEY, false)
        val alarm2isRunning = sharedPreferences.getBoolean(Const.KINYU_NOTI_KEY, false)
        val alarm3isRunning = sharedPreferences.getBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, false)
        binding.swiAll.isChecked= alarm1isRunning && alarm2isRunning && alarm3isRunning
        binding.swiFixExpenseNoti.isChecked= alarm1isRunning
        binding.swiKinyuNoti.isChecked= alarm2isRunning
        binding.swiComparisonNoti.isChecked= alarm3isRunning
    }

    // 화면 클릭 이벤트 설정
    private fun setClickEvent() {
        binding.swiAll.setOnClickListener{
            val isChecked = binding.swiAll.isChecked
            binding.swiFixExpenseNoti.isChecked= isChecked
            binding.swiKinyuNoti.isChecked= isChecked
            binding.swiComparisonNoti.isChecked= isChecked

            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.FIX_EXPENSE_NOTI_KEY, isChecked)
            editor.putBoolean(Const.KINYU_NOTI_KEY, isChecked)
            editor.putBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()

            updateNotification(Const.FIX_EXPENSE_NOTI_KEY)
            updateNotification(Const.KINYU_NOTI_KEY)
            updateNotification(Const.COMPARISON_EXPENSE_NOTI_KEY)
        }

        binding.swiFixExpenseNoti.setOnClickListener{
            val isChecked = binding.swiFixExpenseNoti.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.FIX_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked= isChecked && binding.swiKinyuNoti.isChecked&& binding.swiComparisonNoti.isChecked

            updateNotification(Const.FIX_EXPENSE_NOTI_KEY)
        }

        binding.swiKinyuNoti.setOnClickListener{
            val isChecked = binding.swiKinyuNoti.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.KINYU_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked= binding.swiFixExpenseNoti.isChecked&& isChecked  && binding.swiComparisonNoti.isChecked
            updateNotification(Const.KINYU_NOTI_KEY)
        }

        binding.swiComparisonNoti.setOnClickListener{
            val isChecked = binding.swiComparisonNoti.isChecked
            val editor = sharedPreferences.edit()
            editor.putBoolean(Const.COMPARISON_EXPENSE_NOTI_KEY, isChecked)
            editor.commit()

            // 각각의 스위치 상태 변화시 switchAll 상태 변화
            binding.swiAll.isChecked= binding.swiFixExpenseNoti.isChecked&& binding.swiKinyuNoti.isChecked&& isChecked

            updateNotification(Const.COMPARISON_EXPENSE_NOTI_KEY)
        }
    }

    // 스위치의 현재 상태를 확인해
    private fun updateNotification(flag: String) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val notificationUtil = NotificationUtil(requireContext())
            when(flag) {
                Const.FIX_EXPENSE_NOTI_KEY -> {
                    if (binding.swiFixExpenseNoti.isChecked) {
                        notificationUtil.setFixExpenseNotification()
                    } else {
                        notificationUtil.cancelFixExpenseNotification()
                    }
                }
                Const.KINYU_NOTI_KEY -> {
                    if (binding.swiKinyuNoti.isChecked) {
                        notificationUtil.setKinyuNotification()
                    } else {
                        notificationUtil.cancelKinyuNotification()
                    }
                }
                Const.COMPARISON_EXPENSE_NOTI_KEY -> {
                    if (binding.swiComparisonNoti.isChecked) {
                        notificationUtil.setComparisonExpenseByMonthly()
                    } else {
                        notificationUtil.cancelComparisonExpenseByMonthly()
                    }
                }
            }

            val str = "binding.swiFixExpenseNoti.isChecked: ${binding.swiFixExpenseNoti.isChecked}\n" +
                    "binding.swiKinyuNoti.isChecked: ${binding.swiKinyuNoti.isChecked}\n" +
                    "binding.swiComparisonNoti.isChecked: ${binding.swiComparisonNoti.isChecked}"

            Log.d(TAG, "$str")
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