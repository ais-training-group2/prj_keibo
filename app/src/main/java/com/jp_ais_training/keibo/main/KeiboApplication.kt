package com.jp_ais_training.keibo.main

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.jp_ais_training.keibo.main.notireceiver.ComparisonExpenseNotiReceiver
import com.jp_ais_training.keibo.main.notireceiver.KinyuNotiReceiver
import java.util.*


open class KeiboApplication: Application() {

    private lateinit var alarmManager: AlarmManager
    private val TAG = this::class.java.simpleName.toString()

    override fun onCreate() {
        super.onCreate()

        // Notification Channel 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil(this).createChannels()
        }

        // AlarmManagert 생성 (특정 시간을 인식하는 BroadCast 생성 클래스)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // 가계부 기입 요청 알림 생성
        setKinyuNotification()

        // 월말 지출 비교 알림 생성
        setComparisonExpenseByMonthly()
    }

    // 정기 고정 지출 알림
    private fun setFixExpenseNotification() {

    }

    // 가계부 기입 요청 알림
    // 매일 21시 DB 확인 및 필요시 알림
    private fun setKinyuNotification() {
        val intent = Intent(this, KinyuNotiReceiver::class.java)
        val calendar = Calendar.getInstance()

        // 21시00분00초
        calendar.set(Calendar.HOUR_OF_DAY, Const.NOTI_HOUR_OF_DAY_21)  // 21시
        calendar.set(Calendar.MINUTE, Const.NOTI_MINUTE_ZERO)        // 00분
        calendar.set(Calendar.SECOND, Const.NOTI_SECOND_ZERO)        // 00초
        calendar.set(Calendar.MILLISECOND, Const.NOTI_MILLISECOND_ZERO)

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(this, Const.NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE, intent, Const.NOTI_RECEIVER_PENDING_INTENT_FLAGS)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    // 월말 지출 비교 알림
    // 매월 25일 21시 DB 확인 및 알림
    private fun setComparisonExpenseByMonthly() {
        val intent = Intent(this, ComparisonExpenseNotiReceiver::class.java)
        val calendar = Calendar.getInstance()

        // 매월 25일 21시00분00초
        calendar.set(Calendar.DAY_OF_MONTH, Const.NOTI_DAY_OF_MONTH_25) // 25일
        calendar.set(Calendar.HOUR_OF_DAY, Const.NOTI_HOUR_OF_DAY_21)  // 21시
        calendar.set(Calendar.MINUTE, Const.NOTI_MINUTE_ZERO)        // 00분
        calendar.set(Calendar.SECOND, Const.NOTI_SECOND_ZERO)        // 00초
        calendar.set(Calendar.MILLISECOND, Const.NOTI_MILLISECOND_ZERO)

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(this, Const.NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE, intent, Const.NOTI_RECEIVER_PENDING_INTENT_FLAGS)

        // calendar.getActualMaximum(Calendar.DAY_OF_MONTH)는 매달 달라져야함, 정확히 25일이 아닐 수 있음. 추후 수정 필요
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * calendar.getActualMaximum(Calendar.DAY_OF_MONTH), pendingIntent)

    }

}