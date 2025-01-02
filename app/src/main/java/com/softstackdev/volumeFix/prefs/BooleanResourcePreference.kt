package com.softstackdev.volumeFix.prefs

/**
 * Created by Nena_Schmidt on 22.08.2019.
 */
class BooleanResourcePreference (
        private val keyResId: Int,
        defaultValue: Boolean

) : BooleanPreference("", defaultValue) {

    override fun init(){
        super.init()
        keyResIdStr = AppPrefs.getString(keyResId)
    }
}