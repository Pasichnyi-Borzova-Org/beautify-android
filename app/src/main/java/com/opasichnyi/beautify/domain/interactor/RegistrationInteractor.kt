package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult

class RegistrationInteractor(
    val userRepository: UserRepository,
) {

    suspend operator fun invoke(data: RegisterData): RegisterResult =
        userRepository.registerUser(data)
}