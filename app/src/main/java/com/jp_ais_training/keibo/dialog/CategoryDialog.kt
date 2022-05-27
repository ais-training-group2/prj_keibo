package com.jp_ais_training.keibo.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.jp_ais_training.keibo.R
import com.jp_ais_training.keibo.KeiboApplication
import kotlinx.coroutines.*
import java.lang.Runnable


class CategoryDialog(private val activity: Activity) {

    val app = activity.application as KeiboApplication

    interface ButtonClickListener {
        fun onClicked(id: Int, name: String, type: String)
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }

    private fun displayMainCategoryDialog(
        context: Context,
        title: String,
        body: String,
        dialogActions: MutableList<DialogAction>
    ) {
        ContextCompat.getMainExecutor(context).execute {

            var inflater = LayoutInflater.from(context)

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val inflatedLayout = inflater.inflate(R.layout.custom_alert_dialog, null)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(inflatedLayout)
            inflatedLayout.findViewById<TextView>(R.id.title).text = title
            inflatedLayout.findViewById<TextView>(R.id.body).text = body
            val buttonContainer = inflatedLayout.findViewById<LinearLayout>(R.id.buttonContainer)

            for (i in 0 until dialogActions.size / 3) {
                val dialogActionListLimit3 = ArrayList<DialogAction>()
                for (j in 0..2) {
                    if (i * 3 + j < dialogActions.size)
                        dialogActionListLimit3.add(dialogActions.elementAt(i * 3 + j))
                    else
                        break
                }

                var buttonRow: LinearLayout =
                    inflater.inflate(R.layout.custom_alert_dialog_button, null) as LinearLayout
                buttonRow.gravity = Gravity.CENTER
                var buttons = buttonRow.children as Sequence<Button>

                //button.elementAt(i)
                for (i in 0 until dialogActionListLimit3.size) {
                    buttons.elementAt(i).visibility = View.VISIBLE
                    buttons.elementAt(i).text = dialogActionListLimit3.elementAt(i).text
                    buttons.elementAt(i).setOnClickListener(View.OnClickListener {
                        dialogActionListLimit3.elementAt(i).runnable.run()
                        onClickListener.onClicked(
                            dialogActionListLimit3.elementAt(i).id,
                            dialogActionListLimit3.elementAt(i).text,
                            "main"
                        )
                        callSubCategory(dialogActionListLimit3.elementAt(i).id)
                        dialog.dismiss()
                    })
                }
                buttonContainer.addView(buttonRow, params)
            }
            dialog.show()

        }
    }

    private fun displaySubCategoryDialog(
        context: Context,
        title: String,
        body: String,
        dialogActions: MutableList<DialogAction>
    ) {
        ContextCompat.getMainExecutor(context).execute {

            var inflater = LayoutInflater.from(context)

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val inflatedLayout = inflater.inflate(R.layout.custom_alert_dialog, null)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(inflatedLayout)
            inflatedLayout.findViewById<TextView>(R.id.title).text = title
            inflatedLayout.findViewById<TextView>(R.id.body).text = body
            val buttonContainer = inflatedLayout.findViewById<LinearLayout>(R.id.buttonContainer)

            for (i in 0..dialogActions.size / 3) {
                val dialogActionListLimit3 = ArrayList<DialogAction>()
                for (j in 0..2) {
                    if (i * 3 + j < dialogActions.size)
                        dialogActionListLimit3.add(dialogActions.elementAt(i * 3 + j))
                    else
                        break
                }

                var buttonRow: LinearLayout =
                    inflater.inflate(R.layout.custom_alert_dialog_button, null) as LinearLayout
                buttonRow.gravity = Gravity.CENTER
                var buttons = buttonRow.children as Sequence<Button>

                //button.elementAt(i)
                for (i in 0 until dialogActionListLimit3.size) {
                    buttons.elementAt(i).visibility = View.VISIBLE
                    buttons.elementAt(i).text = dialogActionListLimit3.elementAt(i).text
                    buttons.elementAt(i).setOnClickListener(View.OnClickListener {
                        dialogActionListLimit3.elementAt(i).runnable.run()
                        onClickListener.onClicked(
                            dialogActionListLimit3.elementAt(i).id,
                            dialogActionListLimit3.elementAt(i).text,
                            "sub"
                        )
                        dialog.dismiss()
                    })
                }
                buttonContainer.addView(buttonRow, params)
            }
            dialog.show()

        }
    }

    fun callSubCategory(main_category_id: Int) {
        val dialogActions = mutableListOf<DialogAction>()
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    val subCategoryList = app.db.loadSubCategory(main_category_id)
                    subCategoryList.forEach { subCategory ->
                        dialogActions.add(
                            DialogAction(
                                subCategory.sub_category_id,
                                subCategory.sub_category_name,
                                Runnable {
                                    //액티비티에 네임 보내기 필요에 따라서 id도
                                    // 콜 서브 카테고리 다이얼 로크
                                })
                        )
                    }
                }.join()
                displaySubCategoryDialog(
                    activity,
                    "サブカテゴリ",
                    "カテゴリの追加はプラスボタンを押してください",
                    dialogActions
                )
            }
        }
    }


    fun callMainCategory(iType: Int) {
        val dialogActions = mutableListOf<DialogAction>()
        var type = ""
        type = if (iType == 2)
            "fix"
        else
            "flex"

        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    val mainCategoryList = app.db.loadMainCategory(type)
                    mainCategoryList.forEach { mainCategory ->
                        dialogActions.add(
                            DialogAction(
                                mainCategory.main_category_id,
                                mainCategory.main_category_name,
                                Runnable {
                                })
                        )
                    }
                }.join()
                displayMainCategoryDialog(
                    activity,
                    "メインカテゴリ",
                    "メインカテゴリを選択してください。",
                    dialogActions
                )
            }
        }
    }

}

class DialogAction(var id: Int, var text: String, var runnable: Runnable)
