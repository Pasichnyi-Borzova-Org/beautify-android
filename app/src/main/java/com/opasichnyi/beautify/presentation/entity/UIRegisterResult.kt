package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.UserAccount

sealed class UIRegisterResult {

    data class Success(
        val user: UserAccount,
    ) : UIRegisterResult()

    data class Error(
        val validationResult: UIValidationResult,
    ) : UIRegisterResult()
}