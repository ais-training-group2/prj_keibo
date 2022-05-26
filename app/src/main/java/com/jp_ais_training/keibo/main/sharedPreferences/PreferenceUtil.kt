package  com.jp_ais_training.main.sharedPreferences

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

    fun setAlarm()
    {
        prefs.edit().putBoolean("Alarm", true).apply()
    }

    fun getAlarm(): Boolean
    {
        return prefs.getBoolean("Alarm", false)
    }

    //----------------------------------------------------------------

    fun setAlarm1()
    {
        prefs.edit().putBoolean("Alarm1", true).apply()
    }

    fun getAlarm1(): Boolean
    {
        return prefs.getBoolean("Alarm1", false)
    }

    //----------------------------------------------------------------

    fun setAlarm2()
    {
        prefs.edit().putBoolean("Alarm2", true).apply()
    }

    fun getAlarm2(): Boolean
    {
        return prefs.getBoolean("Alarm2", false)
    }

    //----------------------------------------------------------------

    fun setAlarm3()
    {
        prefs.edit().putBoolean("Alarm3", true).apply()
    }

    fun getAlarm3(): Boolean
    {
        return prefs.getBoolean("Alarm3", false)
    }


}