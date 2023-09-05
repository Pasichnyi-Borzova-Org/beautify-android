package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.repository.UserRepository

class LoggedInUserInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): UserAccount? = userRepository.getLoggedInUser()
}