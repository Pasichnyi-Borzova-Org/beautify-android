package com.opasichnyi.beautify.domain.entity

sealed class RegisterResult {

    data class Success(
        val user: UserAccount,
    ) : RegisterResult()

    data class Error(val reason: RegisterError) : RegisterResult()
}