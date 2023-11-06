package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.interactor.LogoutInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    private val logoutInteractor: LogoutInteractor,
) : BaseViewModel() {

    private val _logoutResultFlow = MutableSharedFlow<Unit>()
    val logoutResultFlow: SharedFlow<Unit> = _logoutResultFlow.asSharedFlow()

    fun logout() = scope.launch {
        logoutInteractor()
        // _logoutResultFlow.emit(Unit)
    }
}
