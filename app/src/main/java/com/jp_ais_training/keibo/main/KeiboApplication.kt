package com.jp_ais_training.keibo.main

import android.app.Application
import android.os.Build
import java.util.*


open class KeiboApplication: Application() {

    private val TAG = this::class.java.simpleName.toString()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val notificationUtil = NotificationUtil(this)

            // Notification Channel 생성
            notificationUtil.createChannels()

            // 정기 고정 지출 알림 생성
            notificationUtil.setFixExpenseNotification()

            // 가계부 기입 요청 알림 생성
            notificationUtil.setKinyuNotification()

            // 월말 지출 비교 알림 생성
            notificationUtil.setComparisonExpenseByMonthly()
        }
    }

}