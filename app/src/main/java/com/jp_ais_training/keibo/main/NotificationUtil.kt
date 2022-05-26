package com.jp_ais_training.keibo.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class NotificationUtil(context: Context): ContextWrapper(context) {

    lateinit var manager: NotificationManager

    fun createChannels() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 정기 고정 지출 알림

        // 가계부 기입 요청 알림 채널 생성
        val kinyuChannel = NotificationChannel(Const.KINYU_CHANNEL_ID, Const.KINYU_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        // 이 채널에 게시된 알림이 알림 표시등(notification light)을 표시해야 하는지 여부를 설정합니다.
        kinyuChannel.enableLights(true)
        // 이 채널에 게시된 알림이 진동해야 하는지 여부를 설정합니다.
        kinyuChannel.enableVibration(true)
        // 이 채널에 게시된 알림에 대한 알림 라이트 색을 설정합니다.
        kinyuChannel.lightColor = Color.GREEN
        // 이 채널에 게시된 알림이 잠금 화면에 표시되는지 여부를 설정합니다.
        kinyuChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(kinyuChannel)

        // 월말 지출 비교 알림
    }


}