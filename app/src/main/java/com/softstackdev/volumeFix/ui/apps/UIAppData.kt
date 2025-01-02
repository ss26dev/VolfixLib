package com.softstackdev.volumeFix.ui.apps

import android.graphics.drawable.Drawable
import com.softstackdev.volumeFix.data.apps.AppData

data class UIAppData(
    val packageName: String,
    val label: String,
    val icon: Drawable?,
    var excluded: Boolean = false
)

fun AppData.toUIAppData(icon: Drawable?, excluded: Boolean = false) =
    UIAppData(this.packageName, this.label, icon, excluded)

fun UIAppData.toAppData() = AppData(this.packageName, this.label)