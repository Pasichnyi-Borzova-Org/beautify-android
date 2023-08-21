package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.presentation.entity.FieldError
import com.opasichnyi.beautify.presentation.entity.UIRegisterResult
import com.opasichnyi.beautify.presentation.entity.UIValidationResult

class DomainRegistrationResultToUIMapper {

    fun mapRegistrationResultToUiRegistrationResult(
        registerResult: RegisterResult,
        validationResult: UIValidationResult,
    ): UIRegisterResult {
        return if (registerResult is RegisterResult.Success && validationResult.isAllValid()) {
            UIRegisterResult.Success(registerResult.user)
        } else {
            UIRegisterResult.Error(
                validationResult.copy(
                    loginError = if (registerResult is RegisterResult.Error)
                        FieldError.LOGIN_IS_OCCUPIED
                    else
                        null
                )
            )
        }
    }
}