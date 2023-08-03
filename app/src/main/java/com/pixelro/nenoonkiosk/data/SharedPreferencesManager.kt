package com.pixelro.nenoonkiosk.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pixelro.nenoonkiosk.NenoonKioskApplication

object SharedPreferencesManager {
    private val pref: SharedPreferences = NenoonKioskApplication.applicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()
    private val gson = Gson()

    private fun putStringAndCommit(key: String, string: String) {
        editor.putString(key, string)
        editor.commit()
    }

    private fun putIntAndCommit(key: String, int: Int) {
        editor.putInt(key, int)
        editor.commit()
    }

    private fun getStringAndReturn(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    private fun getIntAndReturn(key: String): Int {
        return pref.getInt(key, 0)
    }

    fun putString(key: String, value: String) {
        putStringAndCommit(key, value)
    }

    fun putInt(key: String, value: Int) {
        putIntAndCommit(key, value)
    }

    fun getString(key: String): String {
        return getStringAndReturn(key)
    }

    fun getInd(key: String): Int {
        return getIntAndReturn(key)
    }
}