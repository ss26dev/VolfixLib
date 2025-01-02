package com.softstackdev.volumeFix.data.apps

import com.softstackdev.volumeFix.data.db.ExcludedAppsDao
import kotlinx.coroutines.flow.Flow

class ExcludedAppsDataSource(private val appsDao: ExcludedAppsDao) {

    val excludedAppsFlow: Flow<List<AppData>> = appsDao.getAllExcludedApps()

    suspend fun addApp(appData: AppData) {
        appsDao.insert(appData)
    }

    suspend fun delete(appPackage: String) {
        appsDao.delete(appPackage)
    }
}