package com.softstackdev.volumeFix.koin

import androidx.preference.PreferenceManager
import androidx.room.Room
import com.softstackdev.volumeFix.data.apps.AllAppsDataSource
import com.softstackdev.volumeFix.data.apps.AppsRepository
import com.softstackdev.volumeFix.data.apps.AppsRepositoryImpl
import com.softstackdev.volumeFix.data.apps.ExcludedAppsDataSource
import com.softstackdev.volumeFix.data.db.VolfixDatabase
import com.softstackdev.volumeFix.ui.apps.AppsAdapter
import com.softstackdev.volumeFix.ui.apps.AppsViewModel
import com.softstackdev.volumeFix.ui.apps.allApps.AllAppsViewModel
import com.softstackdev.volumeFix.ui.apps.excludedApps.ExcludedAppsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val VOLFIX_DATABASE = "volfix_database"

val libModule = module {
    single<AppsRepository> { AppsRepositoryImpl(get(), get()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            VolfixDatabase::class.java,
            VOLFIX_DATABASE
        ).build()
    }

    single {
        val database = get<VolfixDatabase>()
        database.excludedAppsDao()
    }

    viewModel { AllAppsViewModel(get()) }
    viewModel {
        ExcludedAppsViewModel(
            get(),
            PreferenceManager.getDefaultSharedPreferences(androidContext())
        )
    }

    single { AllAppsDataSource(androidContext()) }
    singleOf(::ExcludedAppsDataSource)

    factory { (viewModel: AppsViewModel) -> AppsAdapter(viewModel) }
}