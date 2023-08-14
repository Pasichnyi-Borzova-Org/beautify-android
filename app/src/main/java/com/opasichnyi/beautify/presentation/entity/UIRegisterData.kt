package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.UserRole

data class UIRegisterData(
    val login: String,
    val name: String,
    val surname: String,
    val password: String,
    val passwordConfirm: String,
    val role: UserRole,
)