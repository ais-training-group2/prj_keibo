package  com.jp_ais_training.keibo.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context)
{
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE )

    fun setTestData()
    {
        prefs.edit().putBoolean("Data", true).apply()
    }

    fun getTestData(): Boolean
    {
        return prefs.getBoolean("Data", false)
    }

    //----------------------------------------------------------------

    fun getAutoAddFixExpenseDate(): String? {
        return prefs.getString("autoAddFixExpenseDate", "")
    }

    fun setAutoAddFixExpenseDate(date: String) {
        return prefs.edit().putString("autoAddFixExpenseDate", date).apply()

    }

}