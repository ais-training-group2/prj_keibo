package com.jp_ais_training.keibo.main

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jp_ais_training.keibo.main.notireceiver.ComparisonExpenseNotiReceiver
import com.jp_ais_training.keibo.main.notireceiver.KinyuNotiReceiver
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class NotificationUtil(context: Context): ContextWrapper(context) {

    private val TAG = this::class.java.simpleName.toString()
    lateinit var manager: NotificationManager

    fun createChannels() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 정기 고정 지출 알림 채널 생성
        val fixExpenseChannel = NotificationChannel(
            Const.FIX_EXPENSE_CHANNEL_ID,
            Const.FIX_EXPENSE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        // 이 채널에 게시된 알림이 알림 표시등(notification light)을 표시해야 하는지 여부를 설정합니다.
        fixExpenseChannel.enableLights(true)
        // 이 채널에 게시된 알림이 진동해야 하는지 여부를 설정합니다.
        fixExpenseChannel.enableVibration(true)
        // 이 채널에 게시된 알림에 대한 알림 라이트 색을 설정합니다.
        fixExpenseChannel.lightColor= Color.GREEN
// 이 채널에 게시된 알림이 잠금 화면에 표시되는지 여부를 설정합니다.
        fixExpenseChannel.lockscreenVisibility= Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(fixExpenseChannel)

        // 가계부 기입 요청 알림 채널 생성
        val kinyuChannel = NotificationChannel(
            Const.KINYU_CHANNEL_ID,
            Const.KINYU_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        // 이 채널에 게시된 알림이 알림 표시등(notification light)을 표시해야 하는지 여부를 설정합니다.
        kinyuChannel.enableLights(true)
        // 이 채널에 게시된 알림이 진동해야 하는지 여부를 설정합니다.
        kinyuChannel.enableVibration(true)
        // 이 채널에 게시된 알림에 대한 알림 라이트 색을 설정합니다.
        kinyuChannel.lightColor= Color.GREEN
// 이 채널에 게시된 알림이 잠금 화면에 표시되는지 여부를 설정합니다.
        kinyuChannel.lockscreenVisibility= Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(kinyuChannel)

        // 월말 지출 비교 알림
        val comparisonChannel = NotificationChannel(
            Const.COMPARISON_CHANNEL_ID,
            Const.COMPARISON_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        // 이 채널에 게시된 알림이 알림 표시등(notification light)을 표시해야 하는지 여부를 설정합니다.
        kinyuChannel.enableLights(true)
        // 이 채널에 게시된 알림이 진동해야 하는지 여부를 설정합니다.
        kinyuChannel.enableVibration(true)
        // 이 채널에 게시된 알림에 대한 알림 라이트 색을 설정합니다.
        kinyuChannel.lightColor= Color.GREEN
// 이 채널에 게시된 알림이 잠금 화면에 표시되는지 여부를 설정합니다.
        kinyuChannel.lockscreenVisibility= Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(comparisonChannel)

    }

    // 정기 고정 지출 알림 설정
    fun setFixExpenseNotification() {

    }

    // 가계부 기입 요청 알림 설정
    // 매일 21시 DB 확인 및 필요시 알림
    fun setKinyuNotification() {

        // AlarmManagert 생성 (특정 시간을 인식하는 BroadCast 생성 클래스)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, KinyuNotiReceiver::class.java)
        val calendar = Calendar.getInstance()

        // 21시00분00초
        calendar.set(Calendar.HOUR_OF_DAY, Const.NOTI_HOUR_OF_DAY_21)  // 21시
        calendar.set(Calendar.MINUTE, Const.NOTI_MINUTE_ZERO)        // 00분
        calendar.set(Calendar.SECOND, Const.NOTI_SECOND_ZERO)        // 00초
        calendar.set(Calendar.MILLISECOND, Const.NOTI_MILLISECOND_ZERO)

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            Const.NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE,
            intent,
            Const.NOTI_RECEIVER_PENDING_INTENT_FLAGS
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    // 월말 지출 비교 알림 설정
    // 매월 25일 21시 DB 확인 및 알림
    fun setComparisonExpenseByMonthly() {

        // AlarmManagert 생성 (특정 시간을 인식하는 BroadCast 생성 클래스)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, ComparisonExpenseNotiReceiver::class.java)
        val calendar = Calendar.getInstance()

        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        // 오늘이 25일이 넘으면, 다음달 25일로 넘김
        if(currentDay > Const.NOTI_DAY_OF_MONTH_25) {
            // 매월 25일 21시00분00초
            calendar.set(Calendar.MONTH, currentMonth + 1)  // 다음달
            calendar.set(Calendar.DAY_OF_MONTH, Const.NOTI_DAY_OF_MONTH_25) // 25일
            calendar.set(Calendar.HOUR_OF_DAY, Const.NOTI_HOUR_OF_DAY_21)  // 21시
            calendar.set(Calendar.MINUTE, Const.NOTI_MINUTE_ZERO)        // 00분
            calendar.set(Calendar.SECOND, Const.NOTI_SECOND_ZERO)        // 00초
            calendar.set(Calendar.MILLISECOND, Const.NOTI_MILLISECOND_ZERO)
        }
        // 오늘이 25일인데, 21시가 넘었을 경우
        else if (currentDay == Const.NOTI_DAY_OF_MONTH_25 && currentHour == Const.NOTI_HOUR_OF_DAY_21) {
            // 21시이후 ~24시까지는 알람을 수행하지 않음
            return
        }
        else {  // 오늘이 25일을 넘지않으면, 이번달 25일 21시로 설정
            // 매월 25일 21시00분00초
            calendar.set(Calendar.DAY_OF_MONTH, Const.NOTI_DAY_OF_MONTH_25) // 25일
            calendar.set(Calendar.HOUR_OF_DAY, Const.NOTI_HOUR_OF_DAY_21)  // 21시
            calendar.set(Calendar.MINUTE, Const.NOTI_MINUTE_ZERO)        // 00분
            calendar.set(Calendar.SECOND, Const.NOTI_SECOND_ZERO)        // 00초
            calendar.set(Calendar.MILLISECOND, Const.NOTI_MILLISECOND_ZERO)
        }

        // 21시00분00초에 실행되는 intent
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            Const.NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE,
            intent,
            Const.NOTI_RECEIVER_PENDING_INTENT_FLAGS
        )

        // calendar.getActualMaximum(Calendar.DAY_OF_MONTH)는 매달 달라져야함, 정확히 25일이 아닐 수 있음. 추후 수정 필요
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY* calendar.getActualMaximum(
                Calendar.DAY_OF_MONTH
            ),
            pendingIntent
        )

    }

    // 정기 고정 지출 알림 취소
    fun cancelFixExpenseNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.deleteNotificationChannel(Const.FIX_EXPENSE_CHANNEL_ID)
    }

    // 월말 지출 비교 알림 취소
    fun cancelKinyuNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.deleteNotificationChannel(Const.KINYU_CHANNEL_ID)
    }

    // 월말 지출 비교 알림 취소
    fun cancelComparisonExpenseByMonthly() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.deleteNotificationChannel(Const.COMPARISON_CHANNEL_ID)
    }
}