package  com.jp_ais_training.main.sharedPreferences


import android.util.Log
import com.jp_ais_training.keibo.main.KeiboApplication
import com.jp_ais_training.keibo.main.model.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*


class MyApplication : KeiboApplication()
{
    lateinit var DB: AppDatabase
    companion object
    {

        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate()
    {
        super.onCreate()

        prefs = PreferenceUtil(applicationContext)
        DB = AppDatabase.getInstance(this)!!

        if(prefs.getTestData() == false) {
            testSet()
            prefs.setTestData()
        }else{
            Log.d("testSet","true")
        }
    }

    fun testSet(){
        CoroutineScope(Dispatchers.Main).async{
            CoroutineScope(Dispatchers.IO).async {

            }.await()
        }
        CoroutineScope(Dispatchers.IO).async {

            val random = Random()

            DB.dao().insertMainCategory()

            for (i in 1 until 20) {
                val main = random.nextInt(9)+1
                DB.dao().insertSubCategory(main, "sub"+i.toString())
            }
            for (i in 1 until 500){
                val month = random.nextInt(9)+1
                val dayF = random.nextInt(3)
                val dayN = random.nextInt(7)+1
                val sub = random.nextInt(15)+1
                val typeR = random.nextInt(2)
                var type = ""
                if(typeR == 1){
                    type="flex"
                }else{
                    type="fix"
                }
                DB.dao().insertII(type,"test"+i.toString(),100,
                    "2022-0"+month.toString()+"-"+dayF.toString()+dayN.toString())

                DB.dao().insertEI(sub,"test"+i.toString(),100,
                    "2022-0"+month.toString()+"-"+dayF.toString()+dayN.toString())
            }
        }
    }
}