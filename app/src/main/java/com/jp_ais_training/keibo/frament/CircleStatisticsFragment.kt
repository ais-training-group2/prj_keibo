package com.jp_ais_training.keibo.frament

import android.R
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleExpandableListAdapter
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.jp_ais_training.keibo.KeiboApplication
import com.jp_ais_training.keibo.databinding.FragmentCircleStatisticsBinding
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
    //DB 선언
    private lateinit var app: KeiboApplication

    var mainCategoryName = ""
    var mainSumBundle = ""

    // 부모 리스트
    val groupData: ArrayList<HashMap<String, String?>> = ArrayList()
    // 자식 리스트
    val childData: ArrayList<ArrayList<HashMap<String, String?>>> = ArrayList()

    //중복 클릭 처리 변수
    private var mLaskClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as KeiboApplication
    }
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

        //원그래프
        pieChart = binding.pieChart
        clearPieChart()

        //최초 기간 표시
        binding.circleKikanTv.setText(
            df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7)+"月01日 ~ "
                    + df.format(cal.time).substring(8,10) +"日"
        )

        //다음달 통계 표시 버튼 최초 숨김
        binding.naviYokugetsuBtn.visibility = View.INVISIBLE
        binding.naviYokugetsuBtn.isEnabled = false

        pickOutMainPrice(df.format(cal.time).substring(0,7))

        //버튼 클릭 이벤트 -----------------------------------------------------------------------------------------
        //전달 통계 표시 버튼
        binding.naviSengetsuBtn.setOnClickListener(){
            //중복 클릭 방지 처리 (2.5초)
            if(SystemClock.elapsedRealtime() - mLaskClickTime<2500){
                return@setOnClickListener
            }
            mLaskClickTime = SystemClock.elapsedRealtime()
            clearPieChart()
            moveLastMonth()

            //전달 기간 표시
            cal.add(Calendar.MONTH, -1)
            binding.circleKikanTv.setText(
                df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7) + "月"
            )
            pickOutMainPrice(df.format(cal.time).substring(0,7))
        }
        //다음달 통계 표시 버튼 -> dateStandard = 0이 될 경우 Invisible 처리
        binding.naviYokugetsuBtn.setOnClickListener(){
            //중복 클릭 방지 처리 (2.5초)
            if(SystemClock.elapsedRealtime() - mLaskClickTime<2500){
                return@setOnClickListener
            }
            mLaskClickTime = SystemClock.elapsedRealtime()
            var mainCategoryName = ""
            clearPieChart()
            moveNextMonth()

            //다음달 기간 표시
            cal.add(Calendar.MONTH, 1)
            if(dateStandard == 0){
                binding.circleKikanTv.setText(
                    df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7)+"月01日 ~ "
                            + df.format(cal.time).substring(8,10) +"日"
                )
                pickOutMainPrice(df.format(cal.time).substring(0,7))

            }else{
                binding.circleKikanTv.setText(
                    df.format(cal.time).substring(0,4) + "年" + df.format(cal.time).substring(5,7) + "月"
                )
                pickOutMainPrice(df.format(cal.time).substring(0,7))
            }
        }

        setParent()
        setChild1()
        setChild2()
        setExListAdapter()

        return binding.root
    }

    //원그래프 옵션 설정
    private fun setPieChartOption(){
        pieChart!!.setUsePercentValues(true)
        pieChart!!.description!!.isEnabled = false
        pieChart!!.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart!!.dragDecelerationFrictionCoef = 0.95f
        pieChart!!.setEntryLabelTextSize(0f)
        pieChart!!.isDrawHoleEnabled = false
        pieChart!!.setHoleColor(Color.WHITE)
        pieChart!!.transparentCircleRadius = 61f
        pieChart!!.legend!!.isEnabled = true //하단 색항목 리스트
        pieChart!!.animateXY(1000, 1000) //초기 애니메이션 설정
    }

    //원그래프 항목 추가
    suspend fun setPieChartItem(setPieItem: String) {
        val arr = setPieItem.split(",")
        val arr2 = mainCategoryName.split(",")
        if(arr.isNotEmpty()){
            for (i in 0 until arr.size-1){
                yValues.add(PieEntry(arr[i].toFloat(), arr2[i]))
            }
        }
        delay(300L)
    }

    //원그래프 데이터 세팅
    private fun setPieChartDataSet(){
        val dataSet = PieDataSet(yValues, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val data = PieData(dataSet)
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

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

        if(dateStandard < 0){
            binding.naviYokugetsuBtn.visibility = View.VISIBLE
            binding.naviYokugetsuBtn.isEnabled = true
        }
    }

    //다음달 이동
    private fun moveNextMonth() {
        if(dateStandard == -1){
            dateStandard += 1
            binding.naviYokugetsuBtn.visibility = View.INVISIBLE
            binding.naviYokugetsuBtn.isEnabled = false
        }else{
            dateStandard += 1
        }
    }

    //메인 카테고리 기준 DB 결과 값 가격 추출  <--  "yyyy-mm"형식 날짜, 메인 카테고리 번호 입력
    private fun pickOutMainPrice(setDate: String){
        //DB에서 데이터 가져오기
        CoroutineScope(Dispatchers.Main).launch{
            //원그래프 옵션 설정
            setPieChartOption()
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                var sumTest1 = app.db.loadMonthSumMainCategoryEI(setDate)
                //println(app.db.loadMonthSumSubCategoryEI(setDate))

                //초기 string 변수 선언
                var str_data = sumTest1.toString()
                //공백 제거
                str_data = str_data.replace(" ", "")

                val nameSet1 = arrayOf(
                    "公課金,main_id=1,type=fix","生活,main_id=2,type=fix","その他,main_id=3,type=fix",
                    "食費,main_id=4,type=flex","生活,main_id=5,type=flex","余暇,main_id=6,type=flex",
                    "文化,main_id=7,type=flex","自己開発,main_id=8,type=flex","その他,main_id=9,type=flex"
                )
                val nameSet2 = arrayOf(
                    "公課金,","生活,","その他,",
                    "食費,","生活,","余暇,",
                    "文化,","自己開発,","その他,"
                )
                //메인 카테고리명 취득
                for(i in 0..8){
                    if(str_data.contains(nameSet1[i])){
                        mainCategoryName = mainCategoryName.plus(nameSet2[i])
                    }
                }

                //특정 문자열 변경
                str_data = str_data.replace("LoadSumMainCategoryEI(date=", "")
                str_data = str_data.replace("$setDate,price=", "")
                str_data = str_data.replace(",main_name=公課金,main_id=1,type=fix)", "")
                str_data = str_data.replace(",main_name=生活,main_id=2,type=fix)", "")
                str_data = str_data.replace(",main_name=その他,main_id=3,type=fix)", "")
                str_data = str_data.replace(",main_name=食費,main_id=4,type=flex)", "")
                str_data = str_data.replace(",main_name=生活,main_id=5,type=flex)", "")
                str_data = str_data.replace(",main_name=余暇,main_id=6,type=flex)", "")
                str_data = str_data.replace(",main_name=文化,main_id=7,type=flex)", "")
                str_data = str_data.replace(",main_name=自己開発,main_id=8,type=flex)", "")
                str_data = str_data.replace(",main_name=その他,main_id=9,type=flex)", "")
                str_data = str_data.replace("[", "")
                str_data = str_data.replace("]", "")
                mainSumBundle = str_data

                //원그래프 항목, 데이터 설정
                setPieChartItem(mainSumBundle)
                setPieChartDataSet()
            }
        }
    }

    //확장리스트 -----------------------------------------------------------------------------------
    private fun setParent(){
        // 부모 리스트에 요소를 추가
        val groupA: HashMap<String, String?> = HashMap()
        groupA["group"] = "1"
        val groupB: HashMap<String, String?> = HashMap()
        groupB["group"] = "2"

        groupData.add(groupA)
        groupData.add(groupB)
    }

    private fun setChild1(){
        // 자식 리스트에 요소를 추가
        val childListA: ArrayList<HashMap<String, String?>> = ArrayList()

        val childAA: HashMap<String, String?> = HashMap()
        childAA["group"] = "1"
        childAA["name"] = "a"
        childListA.add(childAA)

        val childAB: HashMap<String, String?> = HashMap()
        childAB["group"] = "1"
        childAB["name"] = "b"
        childListA.add(childAB)

        val childAC: HashMap<String, String?> = HashMap()
        childAC["group"] = "1"
        childAC["name"] = "c"
        childListA.add(childAC)

        childData.add(childListA)
    }

    private fun setChild2(){
        // 자식 리스트에 요소를 추가
        val childListB: ArrayList<HashMap<String, String?>> = ArrayList()

        val childBA: HashMap<String, String?> = HashMap()
        childBA["group"] = "2"
        childBA["name"] = "a"
        childListB.add(childBA)

        val childBB: HashMap<String, String?> = HashMap()
        childBB["group"] = "2"
        childBB["name"] = "b"
        childListB.add(childBB)

        val childBC: HashMap<String, String?> = HashMap()
        childBC["group"] = "2"
        childBC["name"] = "c"
        childListB.add(childBC)

        childData.add(childListB)
    }

    private fun setExListAdapter(){
        // 부모 리스트와 자식 리스트를 포함한 어댑터 생성
        val adapter = SimpleExpandableListAdapter(
            context,
            groupData,
            R.layout.simple_expandable_list_item_1,
            arrayOf("group"),
            intArrayOf(R.id.text1),
            childData,
            R.layout.simple_expandable_list_item_2,
            arrayOf("name", "group"),
            intArrayOf(R.id.text1, R.id.text2)
        )

        // ExpandableListView에 Adapter를 설정
        val listView = binding.circleExV
        listView.setAdapter(adapter)
    }

    //--------------------------------------------------------------------------------------------

    override fun onDestroyView() {
        //원그래프 데이터 초기화
        clearPieChart()

        groupData.clear()
        childData.clear()
        binding.circleExV.invalidate()

        //월별 이동 처리 기준 초기화
        dateStandard = 0

        mBinding = null

        super.onDestroyView()
    }
}
