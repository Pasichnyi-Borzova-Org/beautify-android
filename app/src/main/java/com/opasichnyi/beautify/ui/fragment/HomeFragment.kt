package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.View
import com.opasichnyi.beautify.databinding.FragmentHomeBinding
import com.opasichnyi.beautify.presentation.viewmodel.HomeViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.logoutBtn?.setOnClickListener {
            viewModel.logout()
        }
    }
}