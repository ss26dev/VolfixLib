package com.softstackdev.volumeFix

import android.app.Application
import com.softstackdev.volumeFix.koin.libModule
import com.softstackdev.volumeFix.prefs.AppPrefs
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SSApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //init app preferences
        AppPrefs.init(applicationContext)

        startKoin {
            androidLogger()
            androidContext(this@SSApplication)
            modules(libModule)
        }

    }
}