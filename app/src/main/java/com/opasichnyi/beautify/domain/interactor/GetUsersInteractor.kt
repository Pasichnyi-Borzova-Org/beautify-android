package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.repository.UserRepository

class GetUsersInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): List<UserAccount> = userRepository.getAllUsers()
}