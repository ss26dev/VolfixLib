package com.softstackdev.volumeFix.ui.apps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softstackdev.volumeFix.data.apps.AppData
import com.softstackdev.volumeFix.data.apps.AppsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

abstract class AppsViewModel(private val appsRepository: AppsRepository) : ViewModel() {

    // Backing property to avoid state updates from other classes
    protected val _uiState = MutableLiveData<UIState<List<UIAppData>>>(UIState.Loading())

    // The UI collects from this StateFlow to get its state updates
    val uiState: LiveData<UIState<List<UIAppData>>> = _uiState

    abstract fun onAppClick(appData: UIAppData): Job

    protected fun loadApps(appsSource: Flow<List<AppData>>) =
        CoroutineScope(Dispatchers.IO).launch {
            appsSource
                .catch {
                    updateUIState(UIState.Failure("Failed to load apps"))
                }
                .collect { appDataList ->
                    val uiAppDataList = convertToUIAppData(appDataList)

                    updateUIState(UIState.Data(uiAppDataList))
                }
        }

    protected fun updateUIState(uiState: UIState<List<UIAppData>>) = viewModelScope.launch {
        _uiState.value = uiState
    }

    protected open fun convertToUIAppData(appDataList: List<AppData>) = appDataList.map { appData ->
        appData.toUIAppData(appsRepository.getAppIcon(appData.packageName))
    }
}