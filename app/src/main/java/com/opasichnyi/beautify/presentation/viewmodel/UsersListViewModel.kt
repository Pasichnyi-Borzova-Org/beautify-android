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

    private var allUsers = emptyList<UserAccount>()

    fun loadUsers(force: Boolean = false) = scope.launch {
        if (_usersFlow.value.isEmpty() || force) {
            showProgress()
            allUsers = getUsersInteractor()
            _usersFlow.emit(allUsers)
            hideProgress()
        } else {
            _usersFlow.emit(_usersFlow.value)
        }
    }

    fun onUserSelected(user: UserAccount) = scope.launch {
        _selectedUserFlow.emit(user)
    }

    // TODO("Make search aoolicable for big amount of users and pagination - api calls + local")
    fun filterUsers(query: String) = scope.launch {
        _usersFlow.emit(allUsers.filter {
            (it.name + " " + it.surname).contains(query)
        })
    }
}