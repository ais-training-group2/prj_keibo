package com.jp_ais_training.keibo.main

import android.content.SharedPreferences

// 애플리케이션에서 활용할 공통 상수
object Const {

    // 가계부 기입 요청 알림 관련 상수
    val KINYU_CHANNEL_ID = "KINYU"
    val KINYU_CHANNEL_NAME = "KINYU CHANNEL"

    val KINYU_NOTIFICATION = -777
    val IS_INIT_KINYU_NOTI = "isInitKinyuNoti"
    val NOTI_KEY = "Noti"
    val FIX_EXPENSE_NOTI_KEY = "FixExpenseNoti"
    val KINYU_NOTI_KEY = "KinyuNoti"
    val COMPARISON_EXPENSE_NOTI_KEY = "ComparisonExpense"

    val KINYU_NOTI_CONTENT_TITLE = "家計簿記入要請通知"
    val KINYU_NOTI_CONTENT_TEXT = "家計簿記入が届いてありません。記入してください!"

    val KINYU_MAIN_ACTIVITY_EXTRA_TYPE = "type"
    val KINYU_MAIN_ACTIVITY_EXTRA_YEAR = "year"
    val KINYU_MAIN_ACTIVITY_EXTRA_MONTH = "month"
    val KINYU_MAIN_ACTIVITY_EXTRA_DAY = "day"

    val KINYU_MAIN_ACTIVITY_PENDING_INTENT_REQUEST_CODE = 0
    val KINYU_MAIN_ACTIVITY_PENDING_INTENT_FLAGS = 0

    val KINYU_NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE = 0
    val KINYU_NOTI_RECEIVER_PENDING_INTENT_FLAGS = 0

    val KINYU_NOTI_HOUR_OF_DAY = 21
    val KINYU_NOTI_MINUTE = 0
    val KINYU_NOTI_SECOND = 0
    val KINYU_NOTI_MILLISECOND = 0
}