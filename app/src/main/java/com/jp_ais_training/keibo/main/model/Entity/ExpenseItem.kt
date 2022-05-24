package com.jp_ais_training.keibo.main.model.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "ExpenseItem", foreignKeys = [ForeignKey(
    entity = SubCategory::class,
    parentColumns = ["sub_category_id"],
    childColumns = ["sub_category_id"],
)])
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true) val expense_item_id: Int,
    val  sub_category_id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "datetime") val datetime: String
)