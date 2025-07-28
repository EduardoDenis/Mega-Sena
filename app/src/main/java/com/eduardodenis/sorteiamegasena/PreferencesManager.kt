package com.eduardodenis.sorteiamegasena

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs = context.getSharedPreferences("megasena", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        prefs.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getData(key: String): String {
        return prefs.getString(key, "") ?: ""
    }

}
