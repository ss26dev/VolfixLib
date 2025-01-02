package com.softstackdev.volumeFix.data.apps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppData(
    @PrimaryKey val packageName: String,
    @ColumnInfo(name = "label") val label: String,
)
