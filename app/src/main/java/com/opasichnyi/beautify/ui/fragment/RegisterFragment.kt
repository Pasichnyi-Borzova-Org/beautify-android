package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.View
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentRegisterBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserRole
import com.opasichnyi.beautify.presentation.entity.UIRegisterData
import com.opasichnyi.beautify.presentation.entity.UIRegisterResult
import com.opasichnyi.beautify.presentation.entity.UIValidationResult
import com.opasichnyi.beautify.presentation.viewmodel.RegisterViewModel
import com.opasichnyi.beautify.ui.activity.HomeActivity
import com.opasichnyi.beautify.ui.base.BaseFragment

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.registerBtn?.setOnClickListener {
            viewModel.tryRegister(
                UIRegisterData(
                    login = binding?.loginEt?.text.toString(),
                    name = binding?.nameEt?.text.toString(),
                    surname = binding?.surnameEt?.text.toString(),
                    password = binding?.passwordEt?.text.toString(),
                    passwordConfirm = binding?.passwordConfirmEt?.text.toString(),
                    role = if (binding?.isMasterCheckbox?.isChecked == true)
                        UserRole.MASTER
                    else
                        UserRole.CLIENT,
                )
            )
        }
    }

    override fun listenViewModel(viewModel: RegisterViewModel, binding: FragmentRegisterBinding) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.registrationResult.collect {
                when (it) {
                    is UIRegisterResult.Error -> setValidationResult(it.validationResult)
                    is UIRegisterResult.Success -> showHomeScreen(it.user)
                }

            }
        }
    }

    private fun setValidationResult(registrationError: UIValidationResult) {
        binding?.apply {
            loginEt.error = registrationError.loginError?.let { getString(it.localizedMessageId) }
            nameEt.error =
                registrationError.firstNameError?.let { getString(it.localizedMessageId) }
            surnameEt.error =
                registrationError.secondNameError?.let { getString(it.localizedMessageId) }
            passwordEt.error =
                registrationError.passwordError?.let { getString(it.localizedMessageId) }
            passwordConfirmEt.error =
                registrationError.confirmPasswordError?.let { getString(it.localizedMessageId) }
        }
    }

    private fun showHomeScreen(userAccount: UserAccount) {
        startActivity(HomeActivity.newIntent(requireContext(), userAccount))
        requireActivity().finish()
    }
}