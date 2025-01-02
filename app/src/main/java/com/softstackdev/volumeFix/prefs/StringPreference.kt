package com.softstackdev.volumeFix.prefs

import com.softstackdev.volumeFix.prefs.BaseTypePreference

/**
 * Created by Nena_Schmidt on 22.08.2019.
 */
open class StringPreference(
        keyResIdStr: String,
        defaultValue: String = ""

) : BaseTypePreference<String>(keyResIdStr, defaultValue) {

    override var currentValue: String = ""

    override fun getPrefValue() = AppPrefs.getStringPref(keyResIdStr, defaultValue)
    override fun putPrefValue(value: String) = AppPrefs.putStringPref(keyResIdStr, value)

}