package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository,
) : BaseViewModel() {

    fun logout() =
        scope.launch {
            repository.logoutUser()
        }

}