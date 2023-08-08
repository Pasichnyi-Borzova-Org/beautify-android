package com.opasichnyi.beautify.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.interactor.LoggedInUserInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val loggedInUserInteractor: LoggedInUserInteractor,
) : BaseViewModel() {

    private val _loggedInUserStateFlowAccount = MutableSharedFlow<UserAccount?>()
    val loggedInUserStateFlow = _loggedInUserStateFlowAccount.asSharedFlow()

    private val _loggedInUserLiveDataAccount = LiveEvent<UserAccount?>()
    val loggedInUserLiveDataAccount: LiveData<UserAccount?> get() = _loggedInUserLiveDataAccount

    fun checkLoggedInUser() =
        scope.launch { _loggedInUserLiveDataAccount.value = loggedInUserInteractor() }
}
