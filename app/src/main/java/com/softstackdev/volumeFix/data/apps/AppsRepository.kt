package com.softstackdev.volumeFix.data.apps

import android.graphics.drawable.Drawable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AppsRepository {
    val allApps: Flow<List<AppData>>
    fun getAppIcon(packageName: String): Drawable?
    val excludedApps: Flow<List<AppData>>
    suspend fun excludeApp(appData: AppData)
    suspend fun removeExcludedApp(appPackage: String)
}

val blockingAppsKeywords = "camera".toRegex(RegexOption.IGNORE_CASE)

class AppsRepositoryImpl(
    private val allAppsDataSource: AllAppsDataSource,
    private val excludedAppsDataSource: ExcludedAppsDataSource
) : AppsRepository {

    override val allApps = allAppsDataSource.allAppsFlow

    override val excludedApps: Flow<List<AppData>> = excludedAppsDataSource.excludedAppsFlow
        .map { excludedAppsList ->
            // create default list when we don't have any data in the db
            val safeAppDataList = excludedAppsList.ifEmpty {
                val defaultList = mutableListOf<AppData>()

                allApps.value.forEach { appData ->
                    if (appData.packageName.contains(blockingAppsKeywords)) {
                        defaultList.add(appData)
                        excludeApp(appData)
                    }
                }

                defaultList
            }

            // remove non existing apps from db
            safeAppDataList.filter {
                val appInstalled = allAppsDataSource.isAppInstalled(it.packageName)
                if (!appInstalled) {
                    removeExcludedApp(it.packageName)
                }
                appInstalled
            }
        }

    override fun getAppIcon(packageName: String) = allAppsDataSource.getAppIcon(packageName)

    override suspend fun excludeApp(appData: AppData) {
        excludedAppsDataSource.addApp(appData)
    }

    override suspend fun removeExcludedApp(appPackage: String) {
        // don't remove a default app like Camera.
        if (!appPackage.contains(blockingAppsKeywords)) {
            excludedAppsDataSource.delete(appPackage)
        }
    }
}