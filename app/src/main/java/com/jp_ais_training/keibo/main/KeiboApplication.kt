package com.jp_ais_training.keibo.main

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
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


        val sharedPreferences = getSharedPreferences(Const.KINYU_NOTI_KEY, MODE_PRIVATE)

        // 앱 최초 기동시 단 한번 기입 알림 설정
        val isInitKinyuNoti = sharedPreferences.getBoolean(Const.IS_INIT_KINYU_NOTI, false)

        // 한번도 set한 전 없다면
        if (!isInitKinyuNoti) {
            setKinyuNotification()

            // 한번 set했으니 다시 실행되지 않도록 true로 처리
            sharedPreferences.edit().putBoolean(Const.IS_INIT_KINYU_NOTI, true).commit()
        }
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
        calendar.set(Calendar.HOUR_OF_DAY, Const.KINYU_NOTI_HOUR_OF_DAY)  // 21시
        calendar.set(Calendar.MINUTE, Const.KINYU_NOTI_MINUTE)        // 00분
        calendar.set(Calendar.SECOND, Const.KINYU_NOTI_SECOND)        // 00초
        calendar.set(Calendar.MILLISECOND, Const.KINYU_NOTI_MILLISECOND)

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(this, Const.KINYU_NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE, intent, Const.KINYU_NOTI_RECEIVER_PENDING_INTENT_FLAGS)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    // 월말 지출 비교 알림
    private fun setComparisonExpenseByMonthly() {

    }

}