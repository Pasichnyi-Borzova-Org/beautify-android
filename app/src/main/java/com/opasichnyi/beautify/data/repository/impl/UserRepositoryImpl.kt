package com.opasichnyi.beautify.data.repository.impl

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.mock.MockAccountDataSource
import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.repository.UserRepository

class UserRepositoryImpl(
    private val loggedInUserDatasource: LoggedInUserDatasource,
    private val accountDataSource: MockAccountDataSource,
) : UserRepository {

    override suspend fun getLoggedInUser(): UserAccount? {
        val currentLoggedUsername = loggedInUserDatasource.getLoggedInUser()
        return if (currentLoggedUsername != null) {
            accountDataSource.getAccountByUsername(currentLoggedUsername)
        } else {
            null
        }
    }

    override suspend fun loginUser(username: String, password: String): Result<UserAccount> {
        val userResult = accountDataSource.loginUser(username, password)
        if (userResult.isSuccess) {
            loggedInUserDatasource.saveLoggedInUser(username)
        }
        return userResult
    }

    override suspend fun logoutUser() {
        loggedInUserDatasource.deleteLoggedInUser()
    }

    override suspend fun registerUser(registerData: RegisterData): RegisterResult {
        return accountDataSource.registerUser(registerData).also {
            if (it is RegisterResult.Success) {
                loggedInUserDatasource.saveLoggedInUser(registerData.login)
            }
        }
    }
}