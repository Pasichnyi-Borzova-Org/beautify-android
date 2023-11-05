package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.interactor.GetUserInfoInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIUserInfo
import com.opasichnyi.beautify.presentation.mapper.DomainUserInfoToUIUserInfoMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val getUserInfoInteractor: GetUserInfoInteractor,
    private val userInfoMapper: DomainUserInfoToUIUserInfoMapper,
) : BaseViewModel() {

    private val _selectedUserInfoFlow = MutableSharedFlow<UIUserInfo>()
    val selectedUserInfoFlow: SharedFlow<UIUserInfo> =
        _selectedUserInfoFlow.asSharedFlow()

    fun onUserLoaded(user: UserAccount) = scope.launch {
        _selectedUserInfoFlow.emit(
            userInfoMapper.mapDomainUserInfoToUI(
                getUserInfoInteractor(user)
            )
        )
    }
}