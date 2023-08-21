package com.opasichnyi.beautify.domain.entity

sealed class RegisterResult {

    data class Success(
        val user: UserAccount
    ) : RegisterResult()

    data object Error : RegisterResult()
}