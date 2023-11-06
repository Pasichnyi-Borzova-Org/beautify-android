package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.repository.UserRepository

class LogoutInteractor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        userRepository.logoutUser()
    }
}