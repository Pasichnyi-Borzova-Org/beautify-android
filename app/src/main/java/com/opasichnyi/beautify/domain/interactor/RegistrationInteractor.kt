package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.repository.UserRepository

class RegistrationInteractor(
    val userRepository: UserRepository,
) {

    suspend operator fun invoke(data: RegisterData): RegisterResult =
        userRepository.registerUser(data)
}