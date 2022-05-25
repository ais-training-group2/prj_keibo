package com.jp_ais_training.keibo.main

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jp_ais_training.keibo.main.notireceiver.KinyuNotiReceiver
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class KeiboApplication: Application() {

    private lateinit var alarmManager: AlarmManager
    private val TAG = this::class.java.simpleName.toString()

    override fun onCreate() {
        super.onCreate()

        // Notification Channel 생성
        NotificationUtil(this).createChannels()

        // AlarmManagert 생성 (특정 시간을 인식하는 BroadCast 생성 클래스)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        setKinyuNotification()
    }

    // 정기 고정 지출 알림
    private fun setFixExpenseNotification() {

    }

    // 가계부 기입 요청 알림
    // 매일 21시 DB 확인 및 필요시 알림
    private fun setKinyuNotification() {
        val intent = Intent(this, KinyuNotiReceiver::class.java)
        val calendar = Calendar.getInstance()

        // KinyuNotiReceiver에서 활용할 날짜
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        intent.putExtra("year", "$year")
        intent.putExtra("month", "$month")
        intent.putExtra("day", "$day")

        // 21시00분00초
        calendar.set(Calendar.HOUR_OF_DAY, 21)  // 21시
        calendar.set(Calendar.MINUTE, 0)        // 00분
        calendar.set(Calendar.SECOND, 0)        // 00초
        calendar.set(Calendar.MILLISECOND, 0)

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    // 월말 지출 비교 알림
    private fun setComparisonExpenseByMonthly() {

    }

}