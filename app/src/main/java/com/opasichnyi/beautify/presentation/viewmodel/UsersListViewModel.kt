package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.interactor.GetUsersInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersListViewModel(
    private val getUsersInteractor: GetUsersInteractor
) : BaseViewModel() {

    private val _usersFlow = MutableStateFlow<List<UserAccount>>(listOf())
    val usersFlow: SharedFlow<List<UserAccount>> =
        _usersFlow.asStateFlow()

    private val _selectedUserFlow = MutableSharedFlow<UserAccount>()
    val selectedUserFlow: SharedFlow<UserAccount> =
        _selectedUserFlow.asSharedFlow()

    fun loadUsers() = scope.launch {
        if (_usersFlow.value.isEmpty()) {
            showProgress()
            val list = getUsersInteractor()
            _usersFlow.emit(list)
            hideProgress()
        } else {
            _usersFlow.emit(_usersFlow.value)
        }
    }

    fun onUserSelected(user: UserAccount) = scope.launch {
        _selectedUserFlow.emit(user)
    }
}