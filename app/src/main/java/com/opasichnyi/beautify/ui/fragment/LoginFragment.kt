package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentLoginBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.viewmodel.LoginViewModel
import com.opasichnyi.beautify.ui.activity.HomeActivity
import com.opasichnyi.beautify.ui.base.BaseFragment
import com.opasichnyi.beautify.util.ext.navigateActionSafe

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        binding?.registerTv?.setOnClickListener {
            showRegisterFragment()
        }

        binding?.loginBtn?.setOnClickListener {
            binding?.loginErrorTv?.visibility = View.INVISIBLE
            binding?.loading?.visibility = View.VISIBLE
            viewModel.login(
                binding?.usernameEt?.text.toString(),
                binding?.passwordEt?.text.toString()
            )
        }
    }

    override fun listenViewModel(viewModel: LoginViewModel, binding: FragmentLoginBinding) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.loginResultFlow.collect { result ->
                if (result.isSuccess) {
                    binding.loading.visibility = View.INVISIBLE
                    showHomePage(result.getOrThrow())
                } else {
                    binding.loading.visibility = View.INVISIBLE
                    showWrongCredentialsWarning()
                }
            }
        }
    }

    private fun showWrongCredentialsWarning() {
        binding?.loginErrorTv?.visibility = View.VISIBLE
    }

    private fun showHomePage(account: UserAccount) {
        startActivity(HomeActivity.newIntent(requireContext(), account))
        requireActivity().finish()
    }

    private fun showRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigateActionSafe(action)
    }
}