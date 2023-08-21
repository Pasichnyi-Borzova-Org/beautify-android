package com.opasichnyi.beautify.presentation.util

import com.opasichnyi.beautify.presentation.entity.FieldError
import com.opasichnyi.beautify.presentation.entity.UIRegisterData
import com.opasichnyi.beautify.presentation.entity.UIValidationResult

class UIRegisterDataValidator {

    fun validate(uiRegisterData: UIRegisterData): UIValidationResult {
        val usernameValidationError = validateUsername(uiRegisterData.login)
        val nameValidationError = validateFirstName(uiRegisterData.name)
        val surnameValidationError = validateSecondName(uiRegisterData.surname)
        val passwordValidationError = validatePassword(uiRegisterData.password)
        val confirmPasswordValidationError =
            validateConfirmPassword(uiRegisterData.password, uiRegisterData.passwordConfirm)

        return UIValidationResult(
            loginError = usernameValidationError,
            firstNameError = nameValidationError,
            secondNameError = surnameValidationError,
            passwordError = passwordValidationError,
            confirmPasswordError = confirmPasswordValidationError,
        )
    }

    private fun validateUsername(username: String): FieldError? {
        return if (username.isEmpty()) {
            FieldError.FIELD_IS_EMPTY
        } else {
            null
        }
    }

    private fun validateFirstName(firstName: String): FieldError? {
        return if (firstName.isEmpty()) {
            FieldError.FIELD_IS_EMPTY
        } else {
            null
        }
    }

    private fun validateSecondName(firstName: String): FieldError? {
        return if (firstName.isEmpty()) {
            FieldError.FIELD_IS_EMPTY
        } else {
            null
        }
    }

    private fun validatePassword(password: String): FieldError? {
        return if (password.isEmpty()) {
            FieldError.FIELD_IS_EMPTY
        } else if (password.length < MIN_PASSWORD_LENGTH) {
            FieldError.PASSWORD_IS_SHORT
        } else {
            null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): FieldError? {
        return if (confirmPassword.isEmpty()) {
            FieldError.FIELD_IS_EMPTY
        } else if (password != confirmPassword) {
            FieldError.PASSWORDS_DO_NOT_MATCH
        } else {
            null
        }
    }

    companion object {

        const val MIN_PASSWORD_LENGTH = 8
    }
}