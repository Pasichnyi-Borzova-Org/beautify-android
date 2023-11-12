package com.opasichnyi.beautify.domain.entity

data class RegisterData(
    val username: String,
    val name: String,
    val surname: String,
    val password: String,
    val role: UserRole,
)