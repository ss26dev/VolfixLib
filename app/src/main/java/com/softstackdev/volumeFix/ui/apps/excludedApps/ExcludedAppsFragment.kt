package com.softstackdev.volumeFix.ui.apps.excludedApps

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.softstackdev.volumeFix.R
import com.softstackdev.volumeFix.databinding.FragmentExcludedAppsBinding
import com.softstackdev.volumeFix.ui.apps.AppsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ExcludedAppsFragment : Fragment(), MenuProvider {

    private var _binding: FragmentExcludedAppsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val excludedAppsViewModel: ExcludedAppsViewModel by activityViewModel()
    private val excludedAppsAdapter: AppsAdapter by inject { parametersOf(excludedAppsViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExcludedAppsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = excludedAppsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupListAdapter()
    }

    private fun setupListAdapter() {
        binding.excludedAppsGridView.appsGridView.adapter = excludedAppsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Add menu items here
        menuInflater.inflate(R.menu.excluded_apps_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.infoMenuItem -> {
                AlertDialog.Builder(requireActivity()).apply {
                    setIcon(R.drawable.ic_outline_info_24)
                    setTitle(R.string.info)
                    setMessage(R.string.excluded_apps_info)
                    setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                    }
                }.create().show()
            }
        }
        return true
    }
}