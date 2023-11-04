package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserInfo
import com.opasichnyi.beautify.domain.repository.UserRepository

class GetUserInfoInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(userAccount: UserAccount): UserInfo =
        userRepository.getUserInfo(userAccount)
}