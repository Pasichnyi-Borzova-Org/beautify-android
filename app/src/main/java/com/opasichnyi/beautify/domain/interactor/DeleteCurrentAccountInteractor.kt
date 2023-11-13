package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.repository.UserRepository

class DeleteCurrentAccountInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke() {
        userRepository.deleteCurrentUser()
    }
}