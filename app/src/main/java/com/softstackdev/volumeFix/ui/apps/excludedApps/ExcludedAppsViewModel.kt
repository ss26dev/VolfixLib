package com.softstackdev.volumeFix.ui.apps.excludedApps

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softstackdev.volumeFix.data.apps.AppsRepository
import com.softstackdev.volumeFix.ui.apps.AppsViewModel
import com.softstackdev.volumeFix.ui.apps.UIAppData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val DONATED = "donated"

class ExcludedAppsViewModel(
    private val appsRepository: AppsRepository,
    private val sharedPrefs: SharedPreferences
) : AppsViewModel(appsRepository) {

    val _loadAllApps = MutableLiveData(sharedPrefs.getBoolean(DONATED, false))
    val loadAllApps: LiveData<Boolean> = _loadAllApps

    init {
        loadApps(appsRepository.excludedApps)
    }

    override fun onAppClick(appData: UIAppData): Job = CoroutineScope(Dispatchers.IO).launch {
        appsRepository.removeExcludedApp(appData.packageName)
    }

    fun loadAllApps() {
        _loadAllApps.value = true
    }

    fun alreadyDonated() {
        sharedPrefs.edit().putBoolean(DONATED, true).apply()
        loadAllApps()
    }
}