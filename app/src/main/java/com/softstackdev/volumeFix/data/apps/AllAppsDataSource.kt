package com.softstackdev.volumeFix.data.apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.drawable.Drawable
import android.os.Build
import kotlinx.coroutines.flow.*

class AllAppsDataSource(private val context: Context) {

    val allAppsFlow: StateFlow<List<AppData>> = MutableStateFlow(getAllAppsPackages())

    private fun getAllAppsPackages(): List<AppData> {
        val packageManager = context.packageManager

        //get a list of installed apps.
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(
                    PackageManager.GET_META_DATA.toLong()
                )
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }

        return packages.mapNotNull {
            if (isPackageValid(it)) {
                AppData(
                    it.packageName,
                    it.loadLabel(packageManager).toString()
                )
            } else {
                null
            }
        }.sortedBy { it.label }
    }

    fun getAppIcon(packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: NameNotFoundException) {
            null
        }
    }

    fun isAppInstalled(packageId: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager
                    .getApplicationInfo(packageId, PackageManager.ApplicationInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getApplicationInfo(packageId, 0)
            }
            true
        } catch (e: NameNotFoundException) {
            false
        }
    }

    /**
     * Return whether the given PackageInfo contains a valid label and icon
     *
     * @param appInfo
     * @return
     */
    private fun isPackageValid(appInfo: ApplicationInfo): Boolean {
        return appInfo.labelRes != 0 && appInfo.icon != 0
    }
}