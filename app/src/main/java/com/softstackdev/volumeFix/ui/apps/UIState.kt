package com.softstackdev.volumeFix.ui.apps

sealed class UIState<out T> {
    data class Loading<T>(val data: T? = null) : UIState<T>()
    data class Data<T>(val data: T) : UIState<T>()
    data class Failure<T>(val error: String) : UIState<T>()
}