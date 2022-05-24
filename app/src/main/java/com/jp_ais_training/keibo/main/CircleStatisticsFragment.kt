package com.jp_ais_training.keibo.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.jp_ais_training.keibo.databinding.FragmentCircleStatisticsBinding

class CircleStatisticsFragment : Fragment() {
        private var mBinding: FragmentCircleStatisticsBinding? = null
        private val binding get() = mBinding!!

        //원그래프 선언
        private var pieChart: PieChart?= null
        //원그래프 항목
        private var yValues = ArrayList<PieEntry>()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            mBinding = FragmentCircleStatisticsBinding.inflate(inflater, container, false)

            //원그래프---------------------------------------------------------------
            pieChart = binding.pieChart

            setPieChartOption()
            setPieChartItem()
            setPieChartDataSet()

            return binding.root
        }

        //원그래프 옵션 설정
        private fun setPieChartOption(){
            pieChart!!.setUsePercentValues(true)
            pieChart!!.description!!.isEnabled = false
            pieChart!!.setExtraOffsets(5f, 10f, 5f, 5f)
            pieChart!!.dragDecelerationFrictionCoef = 0.95f
            pieChart!!.isDrawHoleEnabled = false
            pieChart!!.setHoleColor(Color.WHITE)
            pieChart!!.transparentCircleRadius = 61f
            pieChart!!.legend!!.isEnabled = false //하단 색항목 리스트 제거
            pieChart!!.animateXY(1000, 1000) //초기 애니메이션 설정
        }

        //원그래프 항목 추가
        private fun setPieChartItem(){
            yValues.add(PieEntry(34f, ""))
            yValues.add(PieEntry(23f, ""))
            yValues.add(PieEntry(14f, ""))
            yValues.add(PieEntry(35f, ""))
            yValues.add(PieEntry(40f, ""))
            yValues.add(PieEntry(40f, ""))
        }

        //원그래프 데이터 세팅
        private fun setPieChartDataSet(){
            val dataSet = PieDataSet(yValues, "")
            dataSet.sliceSpace = 3f
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

            val data = PieData(dataSet)
            data.setValueTextSize(0f)

            pieChart?.data = data
        }

        //원그래프 데이터 초기화
        private fun clearPieChart(){
            yValues.clear()
            pieChart!!.invalidate()
            pieChart!!.clear()
        }

        override fun onDestroyView() {
            //원그래프 데이터 초기화
            clearPieChart()

            mBinding = null

            super.onDestroyView()
        }
}

