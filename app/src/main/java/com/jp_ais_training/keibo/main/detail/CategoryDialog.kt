package com.jp_ais_training.keibo.main.detail

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast


class CategoryDialog(context: Context) {

    val ctx = context

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
    }

}