import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.KeyListener
import android.text.method.MovementMethod
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jp_ais_training.keibo.databinding.FragmentContentsBinding
import com.jp_ais_training.keibo.databinding.RecyclerContentsItemBinding
import com.jp_ais_training.keibo.main.Const
import com.jp_ais_training.keibo.main.detail.CategoryDialog
import com.jp_ais_training.keibo.main.model.AppDatabase
import com.jp_ais_training.keibo.main.model.Response.ResponseItem
import com.jp_ais_training.main.sharedPreferences.MyApplication
import kotlinx.coroutines.*
import net.cachapa.expandablelayout.ExpandableLayout
import java.lang.reflect.Field
import java.text.DecimalFormat

class ContentsFragment() : Fragment() {


    private lateinit var app: MyApplication
    private var targetDate = ""
    private var type = -1 // 0 IncomeFix 1 IncomeFlex 2 ExpenseFix 3 ExpenseFlex
    private lateinit var dataList: List<ResponseItem>
    private var parentColor = -1
    private var color = -1

    private var _binding: FragmentContentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        app = requireActivity().application as MyApplication


        val bundle = arguments
        if (bundle != null) {
            targetDate = bundle.getString(Const.TARGET_DATE).toString()
            type = bundle.getInt(Const.TYPE)
        }
        super.onCreate(savedInstanceState)
    }

    private fun loadData() {
        println("LoadData : $type")
        when (type) {
            0 -> {
                dataList = app.db.loadFixII(targetDate)
                parentColor = Color.rgb(255, 204, 204)
                color = Color.rgb(255, 225, 225)
            }
            1 -> {
                dataList = app.db.loadFlexII(targetDate)
                parentColor = Color.rgb(229, 255, 204)
                color = Color.rgb(250, 255, 225)
            }
            2 -> {
                dataList = app.db.loadFixEI(targetDate)
                parentColor = Color.rgb(204, 255, 255)
                color = Color.rgb(225, 255, 255)
            }
            else -> {
                dataList = app.db.loadFlexEI(targetDate)
                parentColor = Color.rgb(229, 204, 255)
                color = Color.rgb(250, 225, 255)
            }
        }
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

        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                loadData()
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
            }
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
                            val tempPrice =
                                holder.price.text.toString().replace(("[^\\d.]").toRegex(), "")
                                    .toInt()
                            var price = if (taxFlag)
                                tempPrice
                            else
                                (tempPrice * 1.1).toInt()
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

                    if (type == 0 || type == 1) {
                        price.setTextColor(Color.BLUE)
                        topLayout.visibility = View.GONE
                    } else {
                        price.setTextColor(Color.RED)
                        taxLayout.visibility = View.VISIBLE
                    }

                }

                val onKeyListener = View.OnKeyListener { _, KeyCode, event ->
                    if (KeyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        closeKeyBoard()
                        name.clearFocus()
                        name.clearComposingText()
                        price.clearFocus()
                        name.clearComposingText()
                        return@OnKeyListener true
                    }
                    false
                }

                val onNameFocusChangeListener = View.OnFocusChangeListener { view, isFocus ->
                    if (isFocus) {
                    } else {
                        name.clearFocus()
                        name.clearComposingText()
                        closeKeyBoard()
                    }
                }

                val onPriceFocusChangeListener = View.OnFocusChangeListener { view, isFocus ->
                    if (position == dataList.size) {
                        if (!isFocus) {
                            val fPrice = if (type == 0 || type == 1) {
                                DecimalFormat("+#,###,###.#").format(price.text.toString().toInt())
                            } else {
                                DecimalFormat("-#,###,###.#").format(price.text.toString().toInt())
                            }
                            price.text = SpannableStringBuilder(fPrice)
                            price.clearFocus()
                            price.clearComposingText()
                            closeKeyBoard()
                        }
                    } else {
                        if (isFocus) {
                            val fPrice =
                                DecimalFormat("#########").format(dataList[position].price)
                            price.text = SpannableStringBuilder(fPrice)
                        } else {
                            val fPrice = if (type == 0 || type == 1) {
                                DecimalFormat("+#,###,###.#").format(price.text.toString().toInt())
                            } else {
                                DecimalFormat("-#,###,###.#").format(price.text.toString().toInt())
                            }
                            price.text = SpannableStringBuilder(fPrice)
                            price.clearFocus()
                            price.clearComposingText()
                            closeKeyBoard()
                        }
                    }
                }

                mainCg.setOnClickListener {
                    CategoryDialog(activity).callSubCategory()
                }

                cardView.setBackgroundColor(Color.WHITE)
                name.setBackgroundColor(Color.WHITE)
                name.setTextColor(Color.BLACK)
                name.setCursorColor(ctx, parentColor)
                name.setOnKeyListener(onKeyListener)
                name.onFocusChangeListener = onNameFocusChangeListener
                price.setBackgroundColor(Color.WHITE)
                price.setCursorColor(ctx, parentColor)
                price.setOnKeyListener(onKeyListener)
                price.onFocusChangeListener = onPriceFocusChangeListener
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

                setActivationItem(false)
            }

            fun closeKeyBoard() {
                val view = activity.currentFocus
                if (view != null) {
                    val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
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
                val price = holder.price.text.toString().replace(("[^\\d.]").toRegex(), "").toInt()
                return if (type == 0 || type == 1) {
                    holder.name.text.toString() != dataList[position].name
                            || price != dataList[position].price
                } else {
                    val taxFlag = holder.taxCheckBox.isChecked
                    var price = if (taxFlag)
                        price
                    else
                        (price * 1.1).toInt()

                    holder.name.text.toString() != dataList[position].name
                            || price != dataList[position].price
                            || holder.mainCg.text.toString() != dataList[position].main_category_name
                            || holder.subCg.text.toString() != dataList[position].sub_category_name

                }

            }

            fun deActivationItem(holder: ViewHolder) {
                closeKeyBoard()

                holder.cardView.isSelected = false
                holder.cardView.setBackgroundColor(Color.WHITE)

                holder.name.isClickable = false
                holder.name.keyListener = null
                holder.name.movementMethod = null
                holder.name.clearFocus()
                holder.name.clearComposingText()

                holder.price.isClickable = false
                holder.price.keyListener = null
                holder.price.movementMethod = null
                holder.price.clearFocus()
                holder.price.clearComposingText()

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

                    name.isClickable = true
                    name.keyListener = nameListener
                    name.movementMethod = nameMethod
                    /*   name.setOnKeyListener(View.OnKeyListener { _, KeyCode, event ->
                           if (KeyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                               closeKeyBoard()

                               name.clearFocus()
                               name.clearComposingText()
                               price.clearFocus()
                               name.clearComposingText()
                               return@OnKeyListener true
                           }
                           false
                       })*/

                    price.isClickable = true
                    price.keyListener = priceListener
                    price.movementMethod = priceMethod
                    /*   price.setOnKeyListener(View.OnKeyListener { _, KeyCode, event ->
                           if (KeyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                               closeKeyBoard()

                               name.clearFocus()
                               name.clearComposingText()
                               price.clearFocus()
                               name.clearComposingText()
                               return@OnKeyListener true
                           }
                           false
                       })*/


                    taxLayout.visibility = View.VISIBLE

                    mainCg.setTextColor(parentColor)
                    mainCg.setBackgroundColor(Color.WHITE)
                    subCg.setTextColor(parentColor)
                    subCg.setBackgroundColor(Color.WHITE)

                } else {
                    closeKeyBoard()

                    cardView.isSelected = false
                    cardView.setBackgroundColor(Color.WHITE)

                    name.isClickable = false
                    name.keyListener = null
                    name.movementMethod = null
                    name.clearFocus()
                    name.clearComposingText()

                    price.isClickable = false
                    price.keyListener = null
                    price.movementMethod = null
                    price.clearFocus()
                    price.clearComposingText()

                    taxLayout.visibility = View.INVISIBLE

                    mainCg.setTextColor(Color.WHITE)
                    mainCg.setBackgroundColor(parentColor)
                    subCg.setTextColor(Color.WHITE)
                    subCg.setBackgroundColor(parentColor)

                }
            }


            fun EditText.setCursorColor(context: Context, color: Int) {
                val editText = this

                val shapeDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setSize(2.dpToPixels(context), 0)
                    setColor(color)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    textCursorDrawable = shapeDrawable
                } else {
                    try {
                        // get the cursor resource id
                        TextView::class.java.getDeclaredField("mCursorDrawableRes").apply {
                            isAccessible = true
                            val drawableResId: Int = getInt(editText)

                            // get the editor
                            val editorField: Field = TextView::class.java
                                .getDeclaredField("mEditor")
                            editorField.isAccessible = true
                            val editor: Any = editorField.get(editText)

                            // get the drawable and set a color filter
                            val drawable: Drawable? = ContextCompat
                                .getDrawable(editText.context, drawableResId)
                            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

                            // set the drawables
                            editor.javaClass.getDeclaredField("mCursorDrawable").apply {
                                isAccessible = true
                                set(editor, arrayOf(drawable, drawable))
                            }
                        }
                    } catch (e: Exception) {
                        // log exception here
                    }
                }


            }

            fun Int.dpToPixels(context: Context): Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
            ).toInt()

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