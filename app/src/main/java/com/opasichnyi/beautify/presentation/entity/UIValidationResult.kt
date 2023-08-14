package com.opasichnyi.beautify.presentation.entity

data class UIValidationResult(
    val loginError: FieldError?,
    val firstNameError: FieldError?,
    val secondNameError: FieldError?,
    val passwordError: FieldError?,
    val confirmPasswordError: FieldError?,
) {
    fun isAllValid(): Boolean =
        loginError == null &&
                firstNameError == null &&
                secondNameError == null &&
                passwordError == null &&
                confirmPasswordError == null
}