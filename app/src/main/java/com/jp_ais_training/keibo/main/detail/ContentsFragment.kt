import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.KeyListener
import android.text.method.MovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jp_ais_training.keibo.R
import com.jp_ais_training.keibo.databinding.FragmentContentsBinding
import com.jp_ais_training.keibo.databinding.FragmentDetailBinding
import com.jp_ais_training.keibo.databinding.RecyclerContentsItemBinding
import com.jp_ais_training.keibo.main.Const
import com.jp_ais_training.keibo.main.model.AppDatabase
import com.jp_ais_training.keibo.main.model.Entity.ExpenseItem
import com.jp_ais_training.keibo.main.model.Entity.IncomeItem
import com.jp_ais_training.keibo.main.model.Entity.SubCategory
import com.jp_ais_training.keibo.main.model.ExActivity
import com.jp_ais_training.keibo.main.model.Response.ResponseItem
import kotlinx.coroutines.*
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ContentsFragment() : Fragment() {
    private lateinit var DB: AppDatabase

    private var targetDate = ""
    private var type = -1 // 0 IncomeFix 1 IncomeFlex 2 ExpenseFix 3 ExpenseFlex
    private lateinit var dataList: List<ResponseItem>
    private var parentColor = -1
    private var color = -1

    private var _binding: FragmentContentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        DB = AppDatabase.getInstance(requireContext())!!

        val bundle = arguments
        if (bundle != null) {
            targetDate = bundle.getString(Const.TARGET_DATE).toString()
            type = bundle.getInt("type")
        }
        println("onCreate : $targetDate")

        CoroutineScope(Dispatchers.IO).async {
            when (type) {
                0 -> {
                    dataList = DB.dao().loadFixII(targetDate)
                    parentColor = Color.rgb(255, 204, 204)
                    color = Color.rgb(255, 225, 225)
                }
                1 -> {
                    dataList = DB.dao().loadFlexII(targetDate)
                    parentColor = Color.rgb(229, 255, 204)
                    color = Color.rgb(250, 255, 225)
                }
                2 -> {
                    dataList = DB.dao().loadFixEI(targetDate)
                    parentColor = Color.rgb(204, 255, 255)
                    color = Color.rgb(225, 255, 255)
                }
                else -> {
                    dataList = DB.dao().loadFlexEI(targetDate)
                    parentColor = Color.rgb(229, 204, 255)
                    color = Color.rgb(250, 225, 255)
                }
            }

        }

/*

        dataArray.add(DB.dao().loadFixII(targetDate))
        dataArray.add(DB.dao().loadFlexEI(targetDate))
        dataArray.add(DB.dao().loadFixEI(targetDate))
        dataArray.add(DB.dao().loadFlexEI(targetDate))

        parentColorArray.add(Color.rgb(255, 204, 204))
        parentColorArray.add(Color.rgb(229, 255, 204))
        parentColorArray.add(Color.rgb(204, 255, 255))
        parentColorArray.add(Color.rgb(229, 204, 255))

        colorArray.add(Color.rgb(255, 225, 225))
        colorArray.add(Color.rgb(250, 255, 225))
        colorArray.add(Color.rgb(225, 255, 255))
        colorArray.add(Color.rgb(250, 225, 255))


*/

        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerContentsView
        recyclerView.setBackgroundColor(color)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = context?.let {
            SimpleAdapter(
                recyclerView,
                dataList,
                parentColor,
                color,
                type,
                targetDate,
                super.requireActivity(),
                it
            )
        }



        return binding.root
    }

    private class SimpleAdapter(
        private val recyclerView: RecyclerView, private val dataList: List<ResponseItem>,
        private val parentColor: Int, private val color: Int,
        private val type: Int, private val targetDate: String,
        private val activity: Activity, private val ctx: Context
    ) : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

        private var selectedItem = UNSELECTED

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding = RecyclerContentsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(itemBinding)

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return dataList.size + 1
        }

        fun addItem(item: ResponseItem) {
            dataList.plus(item)
            this.notifyDataSetChanged()
        }

        inner class ViewHolder(binding: RecyclerContentsItemBinding) :
            RecyclerView.ViewHolder(binding.root),
            View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private val cardView: CardView
            private val name: EditText
            private val price: EditText
            private val mainCg: Button
            private val subCg: Button
            private val taxCheckBox: CheckBox
            private val taxLayout: LinearLayout
            private val topLayout: LinearLayout

            private val nameListener: KeyListener
            private val priceListener: KeyListener
            private val nameMethod: MovementMethod
            private val priceMethod: MovementMethod

            override fun onClick(v: View?) {
                val holder =
                    recyclerView.findViewHolderForAdapterPosition(selectedItem) as ViewHolder?

                if (holder != null) {
                    println("holder")
                    deActivationItem(holder)
                    // 널체크
                    if (nullChecker(holder)) {
                        val position = holder.cardView.tag as Int

                        //마지막 아이템인가?
                        if (position == dataList.size) {
                            println("Insert" + holder.name.text)
                            val taxFlag = holder.taxCheckBox.isChecked
                            var price = if (taxFlag)
                                holder.price.text.toString().toInt()
                            else
                                (holder.price.text.toString().toInt() * 1.1).toInt()
                           /*
                            //DB에 인설트 시키고 리턴값으로 id값 받기
                            val item = ResponseItem(
                                -1,
                                -1,
                                mainCg.text.toString(),
                                subCg.text.toString(),
                                name.text.toString(),
                                price,
                                targetDate
                            )

                            addItem(item)*/

                        } else {
                            //기존 데이터에서 변화된 값 체크
                            if (dataCompare(holder, position)
                            ) {
                                println("Update")
                            }
                        }
                    }
                }

                val position = bindingAdapterPosition
                if (position == selectedItem) {
                    println("DeActive")
                    setActivationItem(false)

                    selectedItem = UNSELECTED

                } else {
                    println("Active")
                    setActivationItem(true)

                    selectedItem = position
                }
            }

            override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
                TODO("Not yet implemented")
            }

            fun bind() {
                val position = bindingAdapterPosition
                val isSelected = position == selectedItem

                if (dataList.size != position) {
                    name.text = SpannableStringBuilder(dataList[position].name)

                    if (type == 0 || type == 1) {
                        val fPrice =
                            DecimalFormat("+#,###,###.#").format(dataList[position].price)
                        price.text = SpannableStringBuilder(fPrice)
                        price.setTextColor(Color.BLUE)
                        topLayout.visibility = View.GONE
                        taxLayout.visibility = View.INVISIBLE
                    } else {
                        val fPrice =
                            DecimalFormat("-#,###,###.#").format(dataList[position].price)
                        price.text = SpannableStringBuilder(fPrice)
                        price.setTextColor(Color.RED)

                        println("MAIN : " + dataList[position].main_category_name)
                        mainCg.text = dataList[position].main_category_name
                        subCg.text = dataList[position].sub_category_name


                        topLayout.visibility = View.VISIBLE
                        taxLayout.visibility = View.VISIBLE
                    }
                } else {
                    name.text = SpannableStringBuilder("")
                    price.text = SpannableStringBuilder("")
                    mainCg.text = "カテゴリ"
                    subCg.text = "サーブカテゴリ"
                    subCg.visibility = View.INVISIBLE
                    taxLayout.visibility = View.VISIBLE
                    if (type == 0 || type == 1)
                        topLayout.visibility = View.GONE
                    else
                        taxLayout.visibility = View.VISIBLE
                }

                cardView.setBackgroundColor(Color.WHITE)
                name.setBackgroundColor(Color.TRANSPARENT)
                name.setTextColor(Color.BLACK)
                price.setBackgroundColor(Color.TRANSPARENT)
                mainCg.setBackgroundColor(parentColor)
                mainCg.setTextColor(Color.WHITE)
                subCg.setBackgroundColor(parentColor)
                subCg.setTextColor(Color.WHITE)
                taxCheckBox.buttonTintList = ColorStateList.valueOf(parentColor)

                cardView.isSelected = isSelected
                cardView.tag = position

            }

            init {
                cardView = binding.cardView
                name = binding.name
                name.maxWidth = name.width
                price = binding.price
                price.maxWidth = price.width
                topLayout = binding.topLayout
                mainCg = binding.mainCategory
                subCg = binding.subCategory
                taxCheckBox = binding.taxCheckBox
                taxLayout = binding.taxLayout
                cardView.setOnClickListener(this)

                nameListener = name.keyListener
                nameMethod = name.movementMethod
                priceListener = price.keyListener
                priceMethod = price.movementMethod

                setActivationItem(true)
            }


            fun nullChecker(holder: ViewHolder): Boolean {

                val result: Boolean
                return if (type == 0 || type == 1) {
                    !holder.name.text.isNullOrBlank()
                            && !holder.price.text.isNullOrBlank()
                } else {
                    !holder.name.text.isNullOrBlank()
                            && !holder.price.text.isNullOrBlank()
                            && holder.mainCg.text.toString() != "カテゴリ"
                            && holder.subCg.text.toString() != "サーブカテゴリ"
                }

            }

            fun dataCompare(holder: ViewHolder, position: Int): Boolean {

                val result: Boolean
                return if (type == 0 || type == 1) {
                    holder.name.text.toString() != dataList[position].name
                            || holder.price.text.toString().toInt() != dataList[position].price

                } else {
                    val taxFlag = holder.taxCheckBox.isChecked
                    var price = if (taxFlag)
                        holder.price.text.toString().toInt()
                    else
                        (holder.price.text.toString().toInt() * 1.1).toInt()

                    holder.name.text.toString() != dataList[position].name
                            || price != dataList[position].price
                            || holder.mainCg.text.toString() != dataList[position].main_category_name
                            || holder.subCg.text.toString() != dataList[position].sub_category_name

                }

            }

            fun deActivationItem(holder: ViewHolder) {

                holder.cardView.isSelected = false
                holder.cardView.setBackgroundColor(Color.WHITE)

                holder.name.isClickable = false
                holder.name.keyListener = null
                holder.name.movementMethod = null

                holder.price.isClickable = false
                holder.price.keyListener = null
                holder.price.movementMethod = null

                holder.taxLayout.visibility = View.INVISIBLE

                holder.mainCg.setTextColor(Color.WHITE)
                holder.mainCg.setBackgroundColor(parentColor)
                holder.subCg.setTextColor(Color.WHITE)
                holder.subCg.setBackgroundColor(parentColor)

            }


            fun setActivationItem(active: Boolean) {
                println("Set Active : $active")
                if (active) {

                    cardView.isSelected = true
                    cardView.setBackgroundColor(color)

                    name.isClickable = false
                    name.keyListener = null
                    name.movementMethod = null

                    price.isClickable = false
                    price.keyListener = null
                    price.movementMethod = null

                    taxLayout.visibility = View.VISIBLE

                    mainCg.setTextColor(parentColor)
                    mainCg.setBackgroundColor(Color.WHITE)
                    subCg.setTextColor(parentColor)
                    subCg.setBackgroundColor(Color.WHITE)

                } else {

                    cardView.isSelected = false
                    cardView.setBackgroundColor(Color.WHITE)

                    name.isClickable = false
                    name.keyListener = null
                    name.movementMethod = null

                    price.isClickable = false
                    price.keyListener = null
                    price.movementMethod = null

                    taxLayout.visibility = View.INVISIBLE

                    mainCg.setTextColor(Color.WHITE)
                    mainCg.setBackgroundColor(parentColor)
                    subCg.setTextColor(Color.WHITE)
                    subCg.setBackgroundColor(parentColor)

                }
            }
        }


        companion object {
            private const val UNSELECTED = -1
        }
    }

    override fun onDestroy() {
        println("onDestroy : $targetDate")
        super.onDestroy()
    }

}