package com.softstackdev.volumeFix.ui.apps.allApps

import com.softstackdev.volumeFix.data.apps.AppData
import com.softstackdev.volumeFix.data.apps.AppsRepository
import com.softstackdev.volumeFix.ui.apps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class AllAppsViewModel(private val appsRepository: AppsRepository) :
    AppsViewModel(appsRepository) {

    private var excludedAppsList: List<AppData> = emptyList()

    init {
        loadApps(appsRepository.allApps)
        listenForExcludedAppsListChange()
    }

    private fun listenForExcludedAppsListChange() =
        CoroutineScope(Dispatchers.IO).launch {
            appsRepository.excludedApps.collect { appDataList ->
                updateUsedApps(appDataList)
            }
        }

    override fun onAppClick(appData: UIAppData): Job = CoroutineScope(Dispatchers.IO).launch {
        if (appData.excluded) {
            appsRepository.removeExcludedApp(appData.packageName)
        } else {
            appsRepository.excludeApp(appData.toAppData())
        }
    }

    private fun updateUsedApps(excludedApps: List<AppData>) {
        excludedAppsList = excludedApps

        if (_uiState.value is UIState.Data) {
            val updatedList = (_uiState.value as UIState.Data).data.map { uiAppData ->
                if (excludedApps.indexOfFirst { uiAppData.packageName == it.packageName } > -1) {
                    uiAppData.copy(excluded = true)
                } else if (uiAppData.excluded) {
                    uiAppData.copy(excluded = false)
                } else
                    uiAppData
            }

            updateUIState(UIState.Data(updatedList))
        }
    }

    override fun convertToUIAppData(appDataList: List<AppData>): List<UIAppData> {
        return appDataList.map { appData ->
            appData.toUIAppData(
                appsRepository.getAppIcon(appData.packageName),
                appData in excludedAppsList
            )
        }
    }
}