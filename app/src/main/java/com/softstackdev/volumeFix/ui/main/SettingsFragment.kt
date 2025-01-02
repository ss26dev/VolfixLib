package com.softstackdev.volumeFix.ui.main

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.softstackdev.volumeFix.R
import com.softstackdev.volumeFix.VolfixAccessibilityService
import com.softstackdev.volumeFix.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        updateVolfixStatus()
        updateDndStatus()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun updateVolfixStatus() {
        val isVolfixEnabled = isAccessibilityServiceEnabled(VolfixAccessibilityService::class.java)

        // use the setOnTouchListener instead of clickListener because click listener is set
        // on the entire button, sometimes excluding the switch component, which doesn't fw the click
        // so we remained without an event when user pressed on the switch component and not on the
        // entire button.
        binding.statusSwitch.apply {
            isChecked = isVolfixEnabled
            setOnTouchListener { _: View, motionEvent: MotionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> onStatusClick()
                }
                true
            }
        }

        binding.tapToEnableTextView.visibility =
            if (isVolfixEnabled) View.INVISIBLE else View.VISIBLE
    }

    private fun updateDndStatus() {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotificationAccessGranted = notificationManager.isNotificationPolicyAccessGranted

        binding.dndSwitch.apply {
            setOnCheckedChangeListener(null)
            isChecked = isNotificationAccessGranted
            setOnCheckedChangeListener { _: CompoundButton, _: Boolean -> onDndClick() }
        }
    }

    fun onStatusClick() {
        if (binding.statusSwitch.isChecked) {
            launchAccessibilitySettings()
        } else {
            AlertDialog.Builder(requireActivity()).apply {
                setIcon(R.drawable.ic_outline_info_24)
                setTitle(R.string.prominent_disclosure_consent_title)
                setMessage(R.string.prominent_disclosure_consent_message)
                setPositiveButton(R.string.agree) { _, _ ->
                    launchAccessibilitySettings()
                }

                setNegativeButton(R.string.not_now) { _, _ ->
                    binding.statusSwitch.isChecked = false
                }
            }.create().show()
        }
    }

    private fun launchAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun onDndClick() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        startActivity(intent)
    }

    fun onPrivacyPolicyClick() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://note/2746298178759219"))
            intent.setPackage("com.facebook.katana")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/notes/soft-stack-dev/volfix-privacy-policy/2746298178759219/")
                )
            )
        }
    }

    fun onExcludedAppsClick() {
        findNavController().navigate(R.id.action_to_ExcludedAppsFragment)
    }

    private fun isAccessibilityServiceEnabled(service: Class<out AccessibilityService?>): Boolean {
        val prefString = Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )

        return prefString != null && prefString.contains("${requireActivity().packageName}/${service.name}")
    }

    fun onInviteFriendsClick() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.app_name)
        )
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, getString(
                R.string.app_recommendation,
                "https://bit.ly/2LzxhQM"
            )
        )

        sendIntent.type = "text/plain"

        startActivity(
            Intent.createChooser(
                sendIntent,
                resources.getText(R.string.invite_friends)
            )
        )
    }
}