package com.jp_ais_training.keibo.frament

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.jp_ais_training.keibo.KeiboApplication
import com.jp_ais_training.keibo.R
import com.jp_ais_training.keibo.databinding.FragmentWeekBarChartBinding
import com.jp_ais_training.keibo.dialog.WeekPickerDialog
import com.jp_ais_training.keibo.model.response.LoadSumEI
import com.jp_ais_training.keibo.model.response.LoadSumII
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class WeekBarChartFragment : Fragment() {
    private var data_ei = ArrayList<LoadSumEI>()
    private var data_ii = ArrayList<LoadSumII>()
    private var _binding: FragmentWeekBarChartBinding? = null
    private val binding get() = _binding!!
    lateinit var app: KeiboApplication
    private var flag: Boolean = true //수입 지출 버튼 구분 플래그
    private var cal = Calendar.getInstance()
    private var year = cal.get(Calendar.YEAR) //캘린더 year 선언
    private var month = cal.get(Calendar.MONTH) + 1 //캘린더 month 선언
    private var week = cal.get(Calendar.WEEK_OF_MONTH)
    private var dayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()
    private lateinit var weekPickerDialog: WeekPickerDialog
    private var strMonth:String = "" //1~9월일때 앞에 0 추가하기 위해 선언
    private var strDayOfMonth:String = "" //1~9월일때 앞에 0 추가하기 위해 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeekBarChartBinding.inflate(inflater, container, false)
        app = requireActivity().application as KeiboApplication

        setDateButton()
        setDBData()
        setEiButton()
        setIiButton()
        weekPickerDialog = WeekPickerDialog(requireContext())
        binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"

        return binding.root
    }

    private fun setDateButton() {
        binding.dateLeftBtn.setOnClickListener() {
            if (strMonth.toInt() == 1 && week == 1) {
                year--
                month = 12
                strMonth = "12"
                week = 4
                binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
            } else {

                if (week == 1) {
                    week = 4
                    month--
                    strMonth = (strMonth.toInt()-1).toString()

                    if (strMonth.toInt()<10){   // 1~9월달 앞에 0추가
                        strMonth = "0"+strMonth
                    }

                    binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
                } else {
                    week--
                    binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
                    cal.get(Calendar.DAY_OF_MONTH)
                }
            }
            cal.set(Calendar.DAY_OF_WEEK,2)
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, week)
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month-1)
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()
            setDBData()
        }

        binding.dateRightBtn.setOnClickListener() {
            if (month == 12 && week == 4) {
                year++
                month = 1
                strMonth = "01"
                week = 1
                binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
            } else {
                if (week == 4) {
                    week = 1
                    month++

                    strMonth = (strMonth.toInt()+1).toString()

                    if (strMonth.toInt()<10){   // 1~9월달 앞에 0추가
                        strMonth = "0"+strMonth
                    }

                    binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
                } else {
                    week++
                    binding.dateYearBtn.text = "${year}年" + "${strMonth}月" + "${week}週"
                    cal.get(Calendar.DAY_OF_MONTH)
                }
            }
            cal.set(Calendar.DAY_OF_WEEK,2)
            cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, week)
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month-1)
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()
            setDBData()
        }

        binding.dateYearBtn.setOnClickListener() {
            showWeekPickerDialog()
        }

    }

    private fun showWeekPickerDialog() {
        weekPickerDialog.setOnClickedListener(object : WeekPickerDialog.ButtonClickListener {
            override fun onClicked(
                strYear: String, strMonth: String, strWeek: String, strDay: String)
            {
                binding.dateYearBtn.text = strYear + strMonth + strWeek
                year = strYear.substring(0, 4).toInt()
                month = strMonth.substring(0, 1).toInt()
                week = strWeek.substring(0, 1).toInt()
                dayOfMonth = strDay.split("~").get(0)

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth.toInt());
                setDBData()
            }
        })
        weekPickerDialog.show()
    }

    private fun setEiButton() {
        binding.eiButton.setOnClickListener() {
            flag = true
            barChart()

        }
    }

    private fun setIiButton() {
        binding.iiButton.setOnClickListener() {
            flag = false
            barChart()
        }
    }

    private fun setDBData() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch() {

                if (month<10){
                    strMonth = "0"+month.toString()
                }else{
                    strMonth = month.toString()
                }

                if (dayOfMonth.toInt()<10){
                    strDayOfMonth = "0"+dayOfMonth
                }else{
                    strDayOfMonth = dayOfMonth
                }

                var set_wk  = year.toString()+"-"+strMonth+"-"+strDayOfMonth
                var data_wk = app.db.setWeek(set_wk)

                data_ei = ArrayList(app.db.loadWeekDaySumEI(year.toString(), data_wk.toString()))
                data_ii = ArrayList(app.db.loadWeekDaySumII(year.toString(), data_wk.toString()))
            }.join()

            barChart()
        }
    }

    private fun barChart() {
        val barchart: BarChart = binding.barchart// barChart 생성
        val entries = ArrayList<BarEntry>()
        var max = 0f
        var day: String
        var price: Float
        var income: Float

        if (flag) {
        for (i in 1..7) {    // 값이 0인 그래프 7개 그림
            entries.add(BarEntry(i.toFloat(), 0.toFloat()))
        }

        for (i in 0..6) {   // DB에 값이 있는 그래프만 다시 그림

            if (i < data_ei.size) {

                day = data_ei.get(i).date!!.split("-").get(2)
                price = data_ei.get(i).price!!.toFloat()

                when (day) {
                    strDayOfMonth -> entries.add(BarEntry(1f, price))
                    (strDayOfMonth.toInt()+1).toString() -> entries.add(BarEntry(2f, price))
                    (strDayOfMonth.toInt()+2).toString() -> entries.add(BarEntry(3f, price))
                    (strDayOfMonth.toInt()+3).toString() -> entries.add(BarEntry(4f, price))
                    (strDayOfMonth.toInt()+4).toString() -> entries.add(BarEntry(5f, price))
                    (strDayOfMonth.toInt()+5).toString() -> entries.add(BarEntry(6f, price))
                    (strDayOfMonth.toInt()+6).toString() -> entries.add(BarEntry(7f, price))
                }
                if (max < price) {
                    max = price + 1000f
                }
            }
        }
        }else{
            for (i in 1..7) {    // 값이 0인 그래프 7개 그림
                entries.add(BarEntry(i.toFloat(), 0.toFloat()))
            }

            for (i in 0..6) {   // DB에 값이 있는 그래프만 다시 그림

                if (i < data_ii.size) {
                    day = data_ii.get(i).date!!.split("-").get(2)
                    income = data_ii.get(i).price!!.toFloat()

                    when (day) {
                        strDayOfMonth -> entries.add(BarEntry(1f, income))
                        (strDayOfMonth.toInt()+1).toString() -> entries.add(BarEntry(2f, income))
                        (strDayOfMonth.toInt()+2).toString() -> entries.add(BarEntry(3f, income))
                        (strDayOfMonth.toInt()+3).toString() -> entries.add(BarEntry(4f, income))
                        (strDayOfMonth.toInt()+4).toString() -> entries.add(BarEntry(5f, income))
                        (strDayOfMonth.toInt()+5).toString() -> entries.add(BarEntry(6f, income))
                        (strDayOfMonth.toInt()+6).toString() -> entries.add(BarEntry(7f, income))
                    }
                    if (max < income) {
                        max = income + 1000f
                    }
                }
            }

        }
        barchart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(7) // 최대 보이는 그래프 개수를 12개로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false) //그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지,

            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = max //지출금액 위에 선을 그리기 위해 맥시멈값 설정 max = price+1000f
                axisMinimum = 0f // 최소값 0
                granularity = 4/max
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.teal_200) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.purple_200) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
                textSize = 10f //라벨 텍스트 크기
                setBackgroundColor(Color.WHITE)
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 0.5f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 10f // 텍스트 크기

                val days = ArrayList<String>()
                    for (i in 0..6)
                    {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month - 1);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth.toInt());
                        cal.add(Calendar.DATE,i)
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        val strDay = day.toString()+"日"
                        days.add(strDay)
                    }
                valueFormatter = XAxisFormatter(days) // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
                setLabelCount(7, false) //x축 라벨 갯수 지정
            }

            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(700) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }
        var set = BarDataSet(entries, "DataSet") // 데이터셋 초기화
        set.color = ContextCompat.getColor(requireContext(), R.color.week) // 바 그래프 색 설정

        val dataSet: ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.5f //막대 너비 설정
        barchart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(true)
            invalidate()
        }
    }

    inner class XAxisFormatter(private val days : ArrayList<String>) :
        ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days[(value-1).toInt()]
        }
    }
}

