package com.jp_ais_training.keibo.main.model.Response

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class loadSumII(
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "price") val price: Int?
)

data class ExpenseItemDetail(
    @PrimaryKey(autoGenerate = true) val expense_item_id: Int,
    @ColumnInfo(name = "sub_category_id") val  sub_category_id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "datetime") val datetime: String,
    @ColumnInfo(name = "main_category_name") val main_category_name: String,
    @ColumnInfo(name = "sub_category_name") val sub_category_name: String
)

data class loadSumEI(
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "price") val price: Int?
)

data class loadSumSubCategoryEI(
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "sub_name") val sub_name: String?,
    @ColumnInfo(name = "main_id") val main_id: Int?
)


data class loadSumMainCategoryEI(
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "main_name") val main_name: String?,
    @ColumnInfo(name = "main_id") val main_id: Int?,
    @ColumnInfo(name = "type") val type: String?
)
