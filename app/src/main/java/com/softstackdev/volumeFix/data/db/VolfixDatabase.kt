package com.softstackdev.volumeFix.data.db

import androidx.room.RoomDatabase
import com.softstackdev.volumeFix.data.apps.AppData

@androidx.room.Database(entities = [AppData::class], version = 1)
abstract class VolfixDatabase : RoomDatabase() {
    abstract fun excludedAppsDao(): ExcludedAppsDao
}