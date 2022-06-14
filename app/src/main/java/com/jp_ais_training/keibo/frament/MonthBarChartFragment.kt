package com.jp_ais_training.keibo.frament


import android.graphics.Color
import android.os.Bundle
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
import com.jp_ais_training.keibo.databinding.FragmentMonthBarChartBinding
import com.jp_ais_training.keibo.dialog.MonthPickerDialog
import com.jp_ais_training.keibo.model.response.LoadSumEI
import com.jp_ais_training.keibo.model.response.LoadSumII
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class MonthBarChartFragment : Fragment() {
    private var data_ei =  ArrayList<LoadSumEI>()
    private var data_ii = ArrayList<LoadSumII>()
    private var _binding: FragmentMonthBarChartBinding? = null
    private val binding get() = _binding!!
    lateinit var app: KeiboApplication
    private var flag:Boolean= true //수입 지출 버튼 구분 플래그
    private var year = Calendar.getInstance().get(Calendar.YEAR) //캘린더 year 선언
    private var month = Calendar.getInstance().get(Calendar.MONTH)+1 //캘린더 month 선언
    private lateinit var monthPickerDialog : MonthPickerDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthBarChartBinding.inflate(inflater, container, false)
        app = requireActivity().application as KeiboApplication

        binding.dateYearBtn.text = "${year}年"+"${month}月"

        setDateButton()
        setDBData()
        setEiButton()
        setIiButton()
        monthPickerDialog = MonthPickerDialog(requireContext())
        return binding.root
    }

    private fun setDateButton() {
        binding.dateLeftBtn.setOnClickListener() {
            if(month==1) {
                year--
                month=12
                binding.dateYearBtn.text = "${year}年" + "${month}月"
            }else{
                month--
                binding.dateYearBtn.text = "${year}年" + "${month}月"
            }
            setDBData()
        }

        binding.dateRightBtn.setOnClickListener() {
            if(month==12) {
                year++
                month=1
                binding.dateYearBtn.text = "${year}年" + "${month}月"
            }else{
                month++
                binding.dateYearBtn.text = "${year}年" + "${month}月"
            }
                setDBData()
        }

        binding.dateYearBtn.setOnClickListener() {
            showMonthPickerDialog()
        }
    }

    private fun showMonthPickerDialog() {
        monthPickerDialog.setOnClickedListener(object : MonthPickerDialog.ButtonClickListener {
            override fun onClicked(strYear: String, strMonth: String) {
                binding.dateYearBtn.text = strYear+strMonth
                year = strYear.substring(0,4).toInt()
                month = strMonth.substring(0,1).toInt()
                setDBData()
            }
        })
        monthPickerDialog.show()
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
                var dateMonth = ""
                if(month<10){
                    dateMonth = "0"+month
                }
                data_ei = ArrayList(app.db.loadWeekSumEI(year.toString()+"-"+dateMonth))
                data_ii = ArrayList(app.db.loadWeekSumII(year.toString()+"-"+dateMonth))
            }.join()
            barChart()
        }
    }

    private fun barChart() {
        val barchart: BarChart = binding.barchart// barChart 생성
        val entries = ArrayList<BarEntry>()
        var max = 0f

        if(flag){
            for (i in 0..4) {
                var week : Float
                var price : Float

                if(i<data_ei.size){
                    week = i.toFloat()+1// 몇 번째주인지
                    price = data_ei.get(i).price!!.toFloat()
                    entries.add(BarEntry(week ,price))
                }else{
                    week = i.toFloat()+1
                    price = 0f
                    entries.add(BarEntry(week ,price))
                }

                if (max < price!!){
                    max = price+1000f
                }
            }
        }else{
            for (i in 0..4) {
                var week : Float
                var income: Float

                if(i<data_ii.size){
                    week = i.toFloat()+1// 첫번째주
                    income = data_ii.get(i).price!!.toFloat()
                    entries.add(BarEntry(week ,income))
                }else{
                    week = i.toFloat()+1
                    income = 0f
                    entries.add(BarEntry(week ,income))
                }

                if (max < income){
                    max = income+1000f
                }
            }
        }
        barchart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(5) // 최대 보이는 그래프 개수를 12개로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false) //그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지,

            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = max //지출금액 위에 선을 그리기 위해 맥시멈값 설정 max = price+1000f
                axisMinimum = 0f // 최소값 0
                granularity = 1000f// 1000 단위마다 선을 그리려고 설정.
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
                valueFormatter = XAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
                setLabelCount(4, false) //x축 라벨 갯수 지정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(700) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }
        var set = BarDataSet(entries, "DataSet") // 데이터셋 초기화
        set.color = ContextCompat.getColor(requireContext(), R.color.month) // 바 그래프 색 설정

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
    inner class XAxisFormatter : ValueFormatter() {
        private val days =
            arrayOf("第1週", "第2週", "第3週", "第4週" , "第5週" )

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
    }
}

