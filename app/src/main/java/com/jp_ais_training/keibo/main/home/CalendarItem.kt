package com.jp_ais_training.keibo.main.home

enum class CalendarItemType {
    Week, Item
}

data class CalendarItem(
    val index: Int,
    val date: Int,
    val income: Int,
    val expense: Int,
)

data class CalendarWeek(val week:String)