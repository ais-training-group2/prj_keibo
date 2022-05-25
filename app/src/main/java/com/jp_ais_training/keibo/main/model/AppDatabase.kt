package com.jp_ais_training.keibo.main.model
import android.content.Context
import androidx.room.*
import com.jp_ais_training.keibo.main.model.Entity.ExpenseItem
import com.jp_ais_training.keibo.main.model.Entity.IncomeItem
import com.jp_ais_training.keibo.main.model.Entity.MainCategory
import com.jp_ais_training.keibo.main.model.Entity.SubCategory


@Database(
    entities = arrayOf(ExpenseItem::class,IncomeItem::class,MainCategory::class,SubCategory::class),
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): DAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database"
                    ).build()
                }
            }
            return instance
        }
    }
}