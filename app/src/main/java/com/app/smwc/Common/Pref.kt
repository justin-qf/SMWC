package com.app.ssn.Common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Pair
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Pref @SuppressLint("CommitPrefEdits") constructor(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val server = "server_url"

    fun Logout() {
        val countryCode: String = locationCountryCode
        editor.clear()
        editor.commit()
        editor.apply()
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false)
        editor.putString(locationCountryCode, countryCode)
        editor.commit()
        editor.apply()
    }

    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, false)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    fun getLocationCountryCode(): String? {
        return pref.getString(locationCountryCode, "")
    }

    fun setLocationCountryCode(isFirstTime: String) {
        editor.putString(locationCountryCode, isFirstTime)
        editor.commit()
    }

    fun getLatitude(): String? {
        return pref.getString(latitude, "")
    }

    fun setLatitude(Latitude: String) {
        editor.putString(latitude, Latitude)
        editor.commit()
    }

    fun getLongitude(): String? {
        return pref.getString(longitude, "")
    }

    fun setLongitude(Longitude: String) {
        editor.putString(longitude, Longitude)
        editor.commit()
    }

    fun getPreviousCode(): Int {
        return pref.getInt("code", 1)
    }

    fun setAppCode(parameters: Int) {
        pref.edit().putInt("code", parameters).apply()
    }

    fun getDay(): Int {
        return pref.getInt("day", 2)
    }

    fun setDay(parameters: Int) {
        pref.edit().putInt("day", parameters).apply()
    }

    fun dayClear(): Int {
        editor.clear()
        editor.commit()
        editor.apply()
        return pref.getInt("day", 2)
    }

    fun getMonth(): Int {
        return pref.getInt("month", 3)
    }

    fun setMonth(parameters: Int) {
        pref.edit().putInt("month", parameters).apply()
    }

    fun getYear(): Int {
        return pref.getInt("year", 4)
    }

    fun setYear(parameters: Int) {
        pref.edit().putInt("year", parameters).apply()
    }


    fun getReferLinkData(): ArrayList<String> {
        val data: String? = pref.getString(referContent, "")
        val list: ArrayList<String> = if (data == null || data.isEmpty()) {
            ArrayList()
        } else {
            val type: Type = object : TypeToken<ArrayList<String>>() {}.type
            Gson().fromJson(data, type)
        }
        return list
    }

    fun setReferData(contentId: String, catId: String, subCatId: String) {
        val dataList: ArrayList<String> = ArrayList()
        dataList.add(contentId)
        dataList.add(catId)
        dataList.add(subCatId)
        editor.putString(referContent, Gson().toJson(dataList))
        editor.commit()
    }

    fun removeSavedList() {
        editor.putString(loginData, "")
        editor.commit()
    }

    fun setServerUrl(url: String?) {
        editor.putString(server, url)
        editor.commit()
    }


    fun recentVisitedCategory(): String? {
        return pref.getString("recentCategory", "0")
    }

    fun setRecentVisitedCategory(url: String?) {
        editor.putString("recentCategory", url)
        editor.commit()
    }


    var isNotificationOn: Boolean
        get() = pref.getBoolean(notification, true)
        set(status) {
            editor.putBoolean(notification, status)
            editor.commit()
        }

    fun setSearchList(contentId: String, addOrRemove: Boolean) {

        val list: ArrayList<String> = getSearchHistory()

        val isFind: String? = list.find { it == contentId }

        if (addOrRemove) { //add

            if (isFind == null) {
                list.add(contentId)
            }
        } else {
            list.remove(isFind)
        }
        editor.putString("localSearch", Gson().toJson(list))
        editor.commit()
    }

    fun getSearchHistory(): ArrayList<String> {
        val data: String? = pref.getString("localSearch", "")
        val list: ArrayList<String> = if (data == null || data.isEmpty()) {
            ArrayList()
        } else {
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            Gson().fromJson(data, type)
        }
        return list
    }

//    fun setUserLoginData(loginList: ArrayList<List>) {
//        editor.putString(loginData, Gson().toJson(loginList))
//        editor.commit()
//    }

//    fun getUserLoginData(): ArrayList<List> {
//        val data: String? = pref.getString(loginData, "")
//        val list: ArrayList<List> = if (data == null || data.isEmpty()) {
//            ArrayList()
//        } else {
//            val type: Type = object : TypeToken<ArrayList<List>>() {}.type
//            Gson().fromJson(data, type)
//        }
//        return list
//    }

    fun clearPlayedVideoList() {
        editor.remove("key")
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "omc"
        private const val USER_TOKEN = "user_token"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val referContent = "referContent"
        private const val locationCountryCode = "locationCountryCode"
        private const val notification = "notification"
        private const val loginData = "login"
        private const val latitude = "Latitude"
        private const val longitude = "Longitude"
    }

    init {
        val PRIVATE_MODE = 0
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}