package com.softstackdev.volumeFix.koin

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        libModule.verify(extraTypes = listOf(android.content.pm.PackageManager::class))
    }
}