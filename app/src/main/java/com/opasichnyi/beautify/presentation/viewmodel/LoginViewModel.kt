package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.interactor.LoginInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginInteractor: LoginInteractor,
) : BaseViewModel() {

    private val _loginResultFlow = MutableSharedFlow<Result<UserAccount>>()
    val loginResultFlow: SharedFlow<Result<UserAccount>> = _loginResultFlow.asSharedFlow()

    fun login(login: String, password: String) = scope.launch {
        showProgress()
        _loginResultFlow.emit(loginInteractor(login, password))
        hideProgress()
    }
}