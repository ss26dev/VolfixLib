package com.softstackdev.volumeFix.prefs

/**
 * Created by Nena_Schmidt on 19.08.2019.
 */
open class BooleanPreference(
        keyResIdStr: String,
        defaultValue: Boolean

) : BaseTypePreference<Boolean>(keyResIdStr, defaultValue) {

    override var currentValue: Boolean = false

    override fun getPrefValue() = AppPrefs.getBooleanPref(keyResIdStr, defaultValue)
    override fun putPrefValue(value: Boolean) = AppPrefs.putBooleanPref(keyResIdStr, value)

}