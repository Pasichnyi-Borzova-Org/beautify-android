package com.opasichnyi.beautify.ui.fragment

import androidx.navigation.fragment.navArgs
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentUserDetailsBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.entity.UIUserInfo
import com.opasichnyi.beautify.presentation.viewmodel.UserDetailsViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class UserDetailsFragment :
    BaseFragment<FragmentUserDetailsBinding, UserDetailsViewModel>() {

    private val args: UserDetailsFragmentArgs by navArgs()

    private val user: UserAccount get() = args.user

    override fun onResume() {
        super.onResume()
        viewModel.onUserLoaded(user)
    }

    override fun listenViewModel(
        viewModel: UserDetailsViewModel,
        binding: FragmentUserDetailsBinding
    ) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.selectedUserInfoFlow.collect {
                updateView(it)
            }
        }
    }

    private fun updateView(userInfo: UIUserInfo) {
        binding?.apply {
            nameSurname.text = context?.getString(
                R.string.name_surname_format,
                userInfo.userAccount.name,
                userInfo.userAccount.surname,
            ) ?: ""
            cityText.text = userInfo.userAccount.city
            ordersNum.text = userInfo.completedOrders
            ratingsNum.text = userInfo.averageRating
            expNum.text = userInfo.experience
        }
    }
}