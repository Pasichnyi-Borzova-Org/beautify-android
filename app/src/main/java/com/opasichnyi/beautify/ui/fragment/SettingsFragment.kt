package com.opasichnyi.beautify.ui.fragment

import android.content.Intent
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentSettingsBinding
import com.opasichnyi.beautify.presentation.viewmodel.SettingsViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override fun onResume() {
        super.onResume()
        initView()
    }

    override fun listenViewModel(viewModel: SettingsViewModel, binding: FragmentSettingsBinding) {
        super.listenViewModel(viewModel, binding)

        repeatOnStart {
            viewModel.logoutResultFlow.collect {
                val packageManager = requireContext().packageManager
                val intent = packageManager.getLaunchIntentForPackage(requireContext().packageName)
                val componentName = intent!!.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                mainIntent.setPackage(requireContext().packageName)
                requireContext().startActivity(mainIntent)
                Runtime.getRuntime().exit(0)
            }
        }
    }

    private fun initView() {
        binding?.logoutButton?.setOnClickListener {
            viewModel.logout()
        }

        binding?.deleteAccountButton?.setOnClickListener {
            viewModel.deleteCurrentAccount()
        }
    }
}