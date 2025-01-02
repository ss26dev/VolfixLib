package com.softstackdev.volumeFix.data.db

import androidx.room.*
import com.softstackdev.volumeFix.data.apps.AppData
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcludedAppsDao {

    @Query("SELECT * FROM appdata ORDER BY label ASC")
    fun getAllExcludedApps(): Flow<List<AppData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(app: AppData)

    @Query("DELETE FROM appdata WHERE packageName = :appPackage")
    suspend fun delete(appPackage: String)
}