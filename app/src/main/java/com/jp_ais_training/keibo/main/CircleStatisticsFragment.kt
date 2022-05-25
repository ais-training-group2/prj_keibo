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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CircleStatisticsFragment : Fragment() {
    private var mBinding: FragmentCircleStatisticsBinding? = null
    private val binding get() = mBinding!!

    //원그래프 선언
    private var pieChart: PieChart?= null
    //원그래프 항목
    private var yValues = ArrayList<PieEntry>()

    //월별 이동 처리 기준
    private var dateStandard = 0

    //칼랜더 변수 선언
    val cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCircleStatisticsBinding.inflate(inflater, container, false)

        //월별 이동 처리 기준
        dateStandard = 0

        //날짜 형식 지정
        cal.time = Date()
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        //최초 기간 표시
        binding.circleKikanTv.setText(
            df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7)+"月01日 ~ "
                    + df.format(cal.time).substring(8,10) +"日"
        )

        //다음달 통계 표시 버튼 최초 숨김
        binding.naviYokugetsuBtn.visibility = View.INVISIBLE
        binding.naviYokugetsuBtn.isEnabled = false


        //원그래프---------------------------------------------------------------
        pieChart = binding.pieChart

        setPieChartOption()
        setPieChartItem()
        setPieChartDataSet()
        //----------------------------------------------------------------------


        //전달 통계 표시 버튼
        //TODO 전달의 지출 데이터가 없을 경우 Invisible 처리
        binding.naviSengetsuBtn.setOnClickListener(){
            moveLastMonth()

            //전달 기간 표시
            cal.add(Calendar.MONTH, -1)
            binding.circleKikanTv.setText(
                df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7) + "月"
            )
        }
        //다음달 통계 표시 버튼 -> dateStandard = 0이 될 경우 Invisible 처리
        binding.naviYokugetsuBtn.setOnClickListener(){
            moveNextMonth()

            //다음달 기간 표시
            cal.add(Calendar.MONTH, 1)
            if(dateStandard == 0){
                binding.circleKikanTv.setText(
                    df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7)+"月01日 ~ "
                    + df.format(cal.time).substring(8,10) +"日"
                )
            }else{
                binding.circleKikanTv.setText(
                    df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7) + "月"
                )
            }
        }

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

    //전달 이동
    private fun moveLastMonth() {
        //초기화면 기준 0 전달 이동시 -1 다음달 이동 시 +1
        dateStandard += -1
        System.out.println(dateStandard)

        if(dateStandard < 0){
            binding.naviYokugetsuBtn.visibility = View.VISIBLE
            binding.naviYokugetsuBtn.isEnabled = true
            System.out.println(dateStandard)
        }
    }

    //다음달 이동
    private fun moveNextMonth() {
        if(dateStandard == -1){
            dateStandard += 1
            System.out.println(dateStandard)
            binding.naviYokugetsuBtn.visibility = View.INVISIBLE
            binding.naviYokugetsuBtn.isEnabled = false
        }else{
            dateStandard += 1
            System.out.println(dateStandard)
        }
    }

    override fun onDestroyView() {
        //원그래프 데이터 초기화
        clearPieChart()

        //월별 이동 처리 기준 초기화
        dateStandard = 0
        System.out.println(dateStandard)

        mBinding = null

        super.onDestroyView()
    }
}

