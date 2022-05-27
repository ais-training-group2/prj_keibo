package com.jp_ais_training.keibo.main.detail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.jp_ais_training.keibo.R
import com.jp_ais_training.main.sharedPreferences.MyApplication
import kotlinx.coroutines.*
import java.lang.Runnable


class CategoryDialog(private val activity: Activity) {

    val app = activity.application as MyApplication


    private fun displayCategoryDialog(
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
                buttonRow.gravity=Gravity.CENTER
                var buttons = buttonRow.children as Sequence<Button>

                //button.elementAt(i)
                for (i in 0 until buttons.count()) {
                    buttons.elementAt(i).text = dialogActionListLimit3.elementAt(i).text
                    buttons.elementAt(i).setOnClickListener(View.OnClickListener {
                        dialogActionListLimit3.elementAt(i).runnable.run()
                        dialog.dismiss()
                    })
                }
                buttonContainer.addView(buttonRow, params)
            }
            dialog.show()
        }
    }

    fun callSubCategory() {
        val dialogActions = mutableListOf<DialogAction>()
        CoroutineScope(Dispatchers.IO).launch {
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    val mainCategoryList = app.db.loadSubCategory()
                    mainCategoryList.forEach { mainCategory ->
                        dialogActions.add(DialogAction(mainCategory.main_category_name, Runnable {
                            //액티비티에 네임 보내기 필요에 따라서 id도
                            // 콜 서브 카테고리 다이얼 로크
                        }))
                    }
                }.join()
                displayCategoryDialog(activity, "メインカテゴリ", "メインカテゴリを選択してください。", dialogActions)

            }
        }

        displayCategoryDialog(activity, "サブカテゴリ", "カテゴリの追加はプラスボタンを押してください", dialogActions)
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
                        dialogActions.add(DialogAction(mainCategory.main_category_name, Runnable {
                            //액티비티에 네임 보내기 필요에 따라서 id도
                            // 콜 서브 카테고리 다이얼 로크
                        }))
                    }
                }.join()
                displayCategoryDialog(activity, "メインカテゴリ", "メインカテゴリを選択してください。", dialogActions)

            }
        }
    }


/*
    fun callMainCategory() {
        val builder = AlertDialog.Builder(ctx)



        builder.setCancelable(true)
        builder.setTitle("メインカテゴリ")


        builder.setItems(
            arrayOf<CharSequence>("button 1", "button 2", "button 3", "button 4")
        ) { _, which -> // The 'which' argument contains the index position of the selected item
            when (which) {
                0 -> Toast.makeText(ctx, "clicked 1", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(ctx, "clicked 2", Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(ctx, "clicked 3", Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(ctx, "clicked 4", Toast.LENGTH_SHORT).show()
            }
        }

        builder.create().show()
    }*/

}

class DialogAction(var text: String, var runnable: Runnable)
class CategoryAction(var text: String, var runnable: Runnable)