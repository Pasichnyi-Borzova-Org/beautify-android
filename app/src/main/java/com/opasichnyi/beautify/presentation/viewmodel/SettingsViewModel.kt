package com.opasichnyi.beautify.presentation.viewmodel

import android.util.Log
import com.opasichnyi.beautify.domain.interactor.DeleteCurrentAccountInteractor
import com.opasichnyi.beautify.domain.interactor.LogoutInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val logoutInteractor: LogoutInteractor,
    private val deleteCurrentAccountInteractor: DeleteCurrentAccountInteractor,
) : BaseViewModel() {

    private val _logoutResultFlow = MutableSharedFlow<Unit>()
    val logoutResultFlow: SharedFlow<Unit> = _logoutResultFlow.asSharedFlow()

    fun logout() = runBlocking {
        logoutInteractor().also {
            delay(500)
            _logoutResultFlow.emit(Unit)
        }

    }

    fun deleteCurrentAccount() = runBlocking {
        deleteCurrentAccountInteractor()
        logout()
    }
}