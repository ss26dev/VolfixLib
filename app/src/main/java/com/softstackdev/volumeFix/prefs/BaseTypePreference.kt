package com.softstackdev.volumeFix.prefs

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Nena_Schmidt on 22.08.2019.
 */
abstract class BaseTypePreference<T : Any>(
        protected var keyResIdStr: String,
        protected val defaultValue: T

) : ReadWriteProperty<Any, T> {

    private var isInitialized = false
    abstract var currentValue: T


    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!isInitialized) {
            init()
            currentValue = getPrefValue()
        }
        return currentValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {

        if (isInitialized) {
            if (value == currentValue) {
                return
            }
        } else {
            init()
        }

        putPrefValue(value)
        currentValue = value
    }

    protected open fun init() {
        isInitialized = true
    }

    abstract fun getPrefValue(): T
    abstract fun putPrefValue(value: T)


}