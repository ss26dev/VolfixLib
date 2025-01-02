package com.softstackdev.volumeFix.prefs

import com.softstackdev.volumeFix.prefs.AppPrefs
import com.softstackdev.volumeFix.prefs.StringPreference

/**
 * Created by Nena_Schmidt on 22.08.2019.
 */
class StringResourcePreference (
        private val keyResId: Int,
        defaultValue: String = ""

) : StringPreference("", defaultValue) {

    override fun init(){
        super.init()
        keyResIdStr = AppPrefs.getString(keyResId)
    }
}