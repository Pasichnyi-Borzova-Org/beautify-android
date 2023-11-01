package com.opasichnyi.beautify.domain.repository

import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.entity.UserAccount

interface UserRepository {

    suspend fun getLoggedInUser(): UserAccount?

    suspend fun loginUser(username: String, password: String): Result<UserAccount>

    suspend fun logoutUser()

    suspend fun registerUser(registerData: RegisterData): RegisterResult

    suspend fun getAllUsers(): List<UserAccount>
}