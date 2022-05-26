package  com.jp_ais_training.main.sharedPreferences


import android.util.Log
import com.jp_ais_training.keibo.main.KeiboApplication
import com.jp_ais_training.keibo.main.model.AppDatabase
import com.jp_ais_training.keibo.main.model.DAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*


class MyApplication : KeiboApplication() {
    lateinit var db: DAO
    lateinit var prefs: PreferenceUtil

    override fun onCreate() {
        super.onCreate()
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
        CoroutineScope(Dispatchers.Main).async {
            CoroutineScope(Dispatchers.IO).async {

            }.await()
        }
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
