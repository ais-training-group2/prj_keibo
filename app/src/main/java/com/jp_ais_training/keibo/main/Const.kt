package com.jp_ais_training.keibo.main

import android.content.SharedPreferences

// 애플리케이션에서 활용할 공통 상수
object Const {


    val KINYU_CHANNEL_ID = "KINYU"
    val KINYU_CHANNEL_NAME = "KINYU CHANNEL"
    val COMPARISON_CHANNEL_ID = "COMPARISON"
    val COMPARISON_CHANNEL_NAME = "COMPARISON CHANNEL"

    val KINYU_NOTIFICATION = -777
    val IS_INIT_KINYU_NOTI = "isInitKinyuNoti"
    val IS_INIT_COMPARISON_NOTI = "isInitComparisonNoti"
    val NOTI_KEY = "Noti"

    val FIX_EXPENSE_NOTI_KEY = "FixExpenseNoti"
    val KINYU_NOTI_KEY = "KinyuNoti"
    val COMPARISON_EXPENSE_NOTI_KEY = "ComparisonExpense"

    val KINYU_NOTI_CONTENT_TITLE = "家計簿記入要請通知"
    val KINYU_NOTI_CONTENT_TEXT = "家計簿記入が届いてありません。記入してください!"

    val COMPARISON_NOTI_CONTENT_TITLE = "月末支出比較通知"
    val COMPARISON_NOTI_CONTENT_TEXT_1 = "前月総支出："
    val COMPARISON_NOTI_CONTENT_TEXT_2 = "今月総支出："
    val COMPARISON_NOTI_CONTENT_TEXT_3 = "前月に比べ "
    val COMPARISON_NOTI_CONTENT_TEXT_4 = "円ほど使いました。"


    val KINYU_MAIN_ACTIVITY_EXTRA_YEAR = "year"
    val KINYU_MAIN_ACTIVITY_EXTRA_MONTH = "month"
    val KINYU_MAIN_ACTIVITY_EXTRA_DAY = "day"

    val PENDING_INTENT_REQUEST_CODE = 0
    val PENDING_INTENT_FLAGS = 0

    val NOTI_RECEIVER_PENDING_INTENT_REQUEST_CODE = 0
    val NOTI_RECEIVER_PENDING_INTENT_FLAGS = 0

    val NOTI_DAY_OF_MONTH_25 = 25
    val NOTI_HOUR_OF_DAY_21 = 21
    val NOTI_MINUTE_ZERO = 0
    val NOTI_SECOND_ZERO = 0
    val NOTI_MILLISECOND_ZERO = 0

    val NOTI_INTENT_TYPE_KEY_WHAT_TO_DO = "WhatToDo"
    val NOTI_INTENT_TYPE_VALUE_GO_TO_SYOUSAI_PAGE = "GoToSyousaiPage"
    val NOTI_INTENT_TYPE_VALUE_GO_TO_BAR_STATISTIC = "GoToBarStatistic"
    val NOTI_INTENT_TYPE_VALUE_NONE= "None"
    const val NULL = "null"

    const val TARGET_DATE = "targetDate"
}