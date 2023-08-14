package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.View
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentHomeBinding
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoggedInUser()

        binding?.logoutBtn?.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun listenViewModel(viewModel: HomeViewModel, binding: FragmentHomeBinding) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.loggedInUserSharedFlow.collect {
                binding.currentUserText.text = it.login
            }
        }
    }
}