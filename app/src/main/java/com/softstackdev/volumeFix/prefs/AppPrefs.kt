package com.softstackdev.volumeFix.prefs

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager

/**
 * Helper class to access shared preferences
 *
 * Created by adrian on 12.01.2017.
 */

object AppPrefs {

    lateinit var prefs: SharedPreferences
    private lateinit var resources: Resources

//    const val CURRENT_LOCATION_ATTR = "location"

    /**
     * This method will initialize the SharedPreferences for the app and must be called from
     * [android.app.Application] subclass
     *
     * @param appContext the app context
     */
    fun init(appContext: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(appContext)
        resources = appContext.resources
    }

    fun getString(keyResId: Int): String {
        return resources.getString(keyResId)
    }


    fun getBooleanPref(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun putBooleanPref(keyResId: Int, value: Boolean) {
        putBooleanPref(
            getString(keyResId),
            value
        )
    }

    fun putBooleanPref(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getStringPref(key: String, defValue: String): String {
        return prefs.getString(key, defValue)!!
    }

    fun putStringPref(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getFloatPref(key: String, defValue: Float): Float {
        return prefs.getFloat(key, defValue)
    }

    fun putFloatPref(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun getLongPref(key: String, defValue: Long): Long {
        return prefs.getLong(key, defValue)
    }

    fun putLongPref(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getIntPref(keyResId: Int, defValue: Int): Int {
        return getIntPref(
            getString(keyResId),
            defValue
        )
    }

    fun getIntPref(key: String, defValue: Int): Int {
        try {
            return prefs.getInt(key, defValue)
        } catch (e: NullPointerException) {
            // we are returning defValue in case of an Exception
        } catch (e: ClassCastException) {
        }

        return defValue
    }

    fun putIntPref(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun removePref(key: String) {
        if (prefs.contains(key)) {
            prefs.edit().remove(key).apply()
        }
    }

    fun prefKeyExists(key: String): Boolean {
        return prefs.contains(key)
    }
}