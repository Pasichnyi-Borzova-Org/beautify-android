package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.R

enum class FieldError(val localizedMessageId: Int) {

    FIELD_IS_EMPTY(R.string.empty_field_message),
    LOGIN_IS_OCCUPIED(R.string.occupied_login_message),
    PASSWORD_IS_SHORT(R.string.password_short_message),
    PASSWORDS_DO_NOT_MATCH(R.string.passwords_different_message),
}