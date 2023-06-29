package com.vovance.ssn.Common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vovance.securemywillcall.activity.LoginActivity.LoginResponseData

class Pref @SuppressLint("CommitPrefEdits") constructor(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    fun Logout() {
        editor.clear()
        editor.commit()
        editor.apply()
        editor.commit()
        editor.apply()
    }

    fun getPreviousCode(): Int {
        return pref.getInt("code", 1)
    }

    fun setAppCode(parameters: Int) {
        pref.edit().putInt("code", parameters).apply()
    }

    fun saveUser(user: LoginResponseData) {
        pref.edit().putString("user", Gson().toJson(user)).apply()
    }

    fun getUser(): LoginResponseData? {
        val data = pref.getString("user", null) ?: return null
        return Gson().fromJson(data, LoginResponseData::class.java)
    }

    companion object {
        private const val PREF_NAME = "SmwC"
    }

    init {
        val PRIVATE_MODE = 0
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}