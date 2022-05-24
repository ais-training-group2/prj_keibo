package com.jp_ais_training.keibo.main

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        Log.d(TAG, "onCreateView")

        setClickEvent()

        return binding.root
    }

    private fun setClickEvent() {
        binding.swiAll.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "swiAll: $isChecked")
        }

        binding.swiAlarm1.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "swiAlarm1: $isChecked")
        }

        binding.swiAlarm2.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "swiAlarm2: $isChecked")
        }

        binding.swiAlarm3.setOnCheckedChangeListener { compoundButton, isChecked ->
            Log.d(TAG, "swiAlarm3: $isChecked")
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