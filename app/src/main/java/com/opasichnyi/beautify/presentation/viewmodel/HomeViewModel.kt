package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository,
) : BaseViewModel() {

    val loggedInUserSharedFlow = MutableSharedFlow<UserAccount>()

    // TODO("Only for test purposes, remove ")
    fun getLoggedInUser() = scope.launch {
        repository.getLoggedInUser()?.let { loggedInUserSharedFlow.emit(it) }
    }

    fun logout() =
        scope.launch {
            repository.logoutUser()
            getLoggedInUser()
        }

}