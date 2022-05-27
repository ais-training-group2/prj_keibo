package com.jp_ais_training.keibo

import android.app.Application
import android.os.Build
import android.util.Log
import com.jp_ais_training.keibo.util.NotificationUtil
import com.jp_ais_training.keibo.db.AppDatabase
import com.jp_ais_training.keibo.db.DAO
import com.jp_ais_training.keibo.util.PreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*


open class KeiboApplication: Application() {

    private val TAG = this::class.java.simpleName.toString()
    lateinit var db: DAO
    lateinit var prefs: PreferenceUtil

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

        db = AppDatabase.getInstance(this)?.dao()!!
        prefs = PreferenceUtil(applicationContext)
        if (!prefs.getTestData()) {
            testSet()
            prefs.setTestData()
        } else {
            Log.d("testSet", "true")
        }

    }

    fun testSet() {
        CoroutineScope(Dispatchers.IO).async {

            val random = Random()

            db.insertMainCategory()

            for (i in 1 until 20) {
                val main = random.nextInt(9) + 1
                db.insertSubCategory(main, "sub" + i.toString())
            }
            for (i in 1 until 500) {
                val month = random.nextInt(9) + 1
                val dayF = random.nextInt(3)
                val dayN = random.nextInt(7) + 1
                val sub = random.nextInt(15) + 1
                val typeR = random.nextInt(2)
                var type = ""
                if (typeR == 1) {
                    type = "flex"
                } else {
                    type = "fix"
                }
                db.insertII(
                    type, "test" + i.toString(), 100,
                    "2022-0" + month.toString() + "-" + dayF.toString() + dayN.toString()
                )

                db.insertEI(
                    sub, "test" + i.toString(), 100,
                    "2022-0" + month.toString() + "-" + dayF.toString() + dayN.toString()
                )
            }
        }
    }
}