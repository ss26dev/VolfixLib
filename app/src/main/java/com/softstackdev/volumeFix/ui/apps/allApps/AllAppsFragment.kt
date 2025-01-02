package com.softstackdev.volumeFix.ui.apps.allApps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.softstackdev.volumeFix.databinding.FragmentAllAppsBinding
import com.softstackdev.volumeFix.ui.apps.AppsAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AllAppsFragment : Fragment() {
    private var _binding: FragmentAllAppsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val appsViewModel: AllAppsViewModel by viewModel()
    private val appsAdapter: AppsAdapter by inject { parametersOf(appsViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAllAppsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = appsViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter() {
        binding.appsGridLayout.appsGridView.adapter = appsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}