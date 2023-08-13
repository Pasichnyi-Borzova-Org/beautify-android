package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.domain.entity.UserAccount

class LoggedInUserInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): UserAccount? = userRepository.getLoggedInUser()
}