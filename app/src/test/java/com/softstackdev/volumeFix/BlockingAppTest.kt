package com.softstackdev.volumeFix

import com.softstackdev.volumeFix.data.apps.blockingAppsKeywords
import org.junit.Assert
import org.junit.Test

class BlockingAppTest {

    private val data = arrayOf(
        Pair("com.google.android.apps.youtube.music", true),
        Pair("com.apple.android.music", true),
        Pair("com.google.android.youtube", true),
        Pair("com.google.android.YOUTUBE", true),
        Pair("com.spotify.music", true),
        Pair("com.softstackdev.volumeFix", false),
        Pair("com.android.systemui", false)
    )

    @Test
    fun testBlockingApps() {
        data.forEach {
            val match = it.first.contains(blockingAppsKeywords)
            Assert.assertEquals(it.second, match)
        }
    }
}