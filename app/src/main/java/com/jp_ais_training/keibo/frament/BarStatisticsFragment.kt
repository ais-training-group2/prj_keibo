package com.jp_ais_training.keibo.frament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jp_ais_training.keibo.R
import com.jp_ais_training.keibo.databinding.FragmentBarStatisticsBinding

class BarStatisticsFragment : Fragment() {

    private var _binding: FragmentBarStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        val barChartFragment1 = YearBarChartFragment()
        val barChartFragment2 = MonthBarChartFragment()
        val barChartFragment3 = WeekBarChartFragment()

        childFragmentManager.beginTransaction().apply {
            add(R.id.fl_year, barChartFragment1).commit()
        }
        childFragmentManager.beginTransaction().apply {
            add(R.id.fl_month, barChartFragment2).commit()
        }
        childFragmentManager.beginTransaction().apply {
            add(R.id.fl_week, barChartFragment3).commit()
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarStatisticsBinding.inflate(inflater, container, false)

        return binding.root

    }
}

