package com.jp_ais_training.keibo.main.detail

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.jp_ais_training.keibo.R


class CategoryDialog(activity: Activity) {

    val activity = activity

    private fun displayAlterDialog(
        context: Context,
        title: String,
        body: String,
        dialogActions: MutableList<DialogAction>
    ) {
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

        dialogActions.forEach { dialogAction ->
            var buttonRow: LinearLayout =
                inflater.inflate(R.layout.custom_alert_dialog_button, null) as LinearLayout
            var button = buttonRow.children as Sequence<Button>

            //button.elementAt(i)
            button.forEach { i ->
                i.text = "a"
                i.setOnClickListener(View.OnClickListener {
                    dialogAction.runnable.run()
                    dialog.dismiss()
                })
            }


            buttonContainer.addView(buttonRow, params)
        }

        dialog.show()
    }

    fun callSubCategory() {
        val dialogActions = mutableListOf<DialogAction>()
        dialogActions.add(DialogAction("a", Runnable {}))
        dialogActions.add(DialogAction("b", Runnable {}))
        dialogActions.add(DialogAction("c", Runnable {}))
        dialogActions.add(DialogAction("a", Runnable {}))
        dialogActions.add(DialogAction("b", Runnable {}))

        displayAlterDialog(activity, "サブカテゴリ", "カテゴリの追加はプラスボタンを押してください", dialogActions)
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