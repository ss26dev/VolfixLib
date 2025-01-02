package com.softstackdev.volumeFix

import android.accessibilityservice.AccessibilityService
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.*
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.softstackdev.volumeFix.data.apps.AppsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class VolfixAccessibilityService : AccessibilityService() {

    private lateinit var audioManager: AudioManager
    private lateinit var notificationManager: NotificationManager
    private var isNotInABlockingApp = false
    private val appsRepository: AppsRepository by inject()
    private var isFirstPress = true

    @Volatile
    private var blockingAppsPackages = "camera".toRegex(RegexOption.IGNORE_CASE)

    @Volatile
    private var waitingForActionUp = true

    override fun onServiceConnected() {
        super.onServiceConnected()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        initBlockingApps()
    }

    private fun initBlockingApps() {
        CoroutineScope(Dispatchers.IO).launch {
            appsRepository.excludedApps.collect {
                val appPackages = StringBuilder()
                it.forEach { appData ->
                    appPackages.append("|${appData.packageName}")
                }

                val appPackagesString = appPackages.removePrefix("|").toString()
                blockingAppsPackages = appPackagesString.toRegex(RegexOption.IGNORE_CASE)
            }
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val action: Int = event.action

        val direction = when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> ADJUST_RAISE
            KeyEvent.KEYCODE_VOLUME_DOWN -> ADJUST_LOWER
            // when other buttons are pressed, we don't process anything and return false to allow
            // the event to pass forward to other apps/system
            else -> return false
        }

        when (action) {
            KeyEvent.ACTION_DOWN -> {
                waitingForActionUp = true
                return actionDownPress(direction)
            }
            KeyEvent.ACTION_UP -> {
                waitingForActionUp = false
            }
        }

        return false
    }

    private fun actionDownPress(direction: Int): Boolean {

        val isNotMediaPlaying = !audioManager.isMusicActive
        val isNotInCall = audioManager.mode == MODE_NORMAL
        // allowDoNotDisturbChange is true if do not disturb is disabled OR user granted the do not disturb change permission
        val allowDoNotDisturbChange =
            notificationManager.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_ALL
                    || notificationManager.isNotificationPolicyAccessGranted

        if (isNotMediaPlaying && isNotInCall && allowDoNotDisturbChange && isNotInABlockingApp) {
            try {
                adjustRingVolume(direction)
                return true
            } catch (e: SecurityException) {
                e.printStackTrace()

                Toast.makeText(
                    baseContext,
                    "Volfix not working! Email: ss26dev@gmail.com",
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    baseContext,
                    "Volfix not working! Email: ss26dev@gmail.com",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return false
    }

    @Throws(SecurityException::class)
    private fun adjustRingVolume(direction: Int) {
        // on first press, we only show the volume status
        if (isFirstPress) {
            isFirstPress = false
            adjustStreamVolume(0)

            // reset isFirstPress flag after 30 seconds
            CoroutineScope(Dispatchers.Default).launch {
                delay(30 * 1000)
                isFirstPress = true
            }
        } else {
            // following pressings, will change the volume
            adjustStreamVolume(direction)
        }

        // continuously adjust the volume while user keeps pressing a volume button
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)

            while (waitingForActionUp) {
                // when we adjust the volume successfully at least once out of multiple times, we return true
                adjustStreamVolume(direction)
                delay(100)
            }
        }
    }

    private fun adjustStreamVolume(direction: Int) {
        audioManager.adjustStreamVolume(STREAM_RING, direction, FLAG_SHOW_UI)
        val ringVolume = audioManager.getStreamVolume(STREAM_RING)
        audioManager.setStreamVolume(STREAM_NOTIFICATION, ringVolume, 0)

    }

    // use onAccessibilityEvent just to receive details about the running package name. While in
    // camera app, users may use their volume keys as zoom controls so we need to disable
    // the service in that case
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName
            if (packageName != null && !packageName.contains("com.android.systemui")) {
                isNotInABlockingApp = !(packageName.contains(blockingAppsPackages))
            }
        }
    }

    override fun onInterrupt() {}
}