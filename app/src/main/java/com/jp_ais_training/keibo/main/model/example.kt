package com.jp_ais_training.keibo.main.model

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ExActivity : AppCompatActivity() {

    private lateinit var DB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DB = AppDatabase.getInstance(this)!!

        //사용시 CoroutineScope 필요
        val start = CoroutineScope(Dispatchers.IO).async {
            var data = DB.dao().loadEI()
        }


    }

}
