package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.domain.entity.UserAccount

class LoginInteractor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(username: String, password: String): Result<UserAccount> =
        userRepository.loginUser(username, password)

}
