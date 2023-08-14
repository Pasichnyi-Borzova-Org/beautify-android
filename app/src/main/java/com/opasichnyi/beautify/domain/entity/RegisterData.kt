package com.opasichnyi.beautify.domain.entity

data class RegisterData(
    val login: String,
    val name: String,
    val surname: String,
    val password: String,
    val role: UserRole,
)