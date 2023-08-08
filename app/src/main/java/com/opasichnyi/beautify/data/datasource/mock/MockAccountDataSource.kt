package com.opasichnyi.beautify.data.datasource.mock

import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.data.exception.LoginFailedException
import kotlinx.coroutines.delay

class MockAccountDataSource {

    suspend fun getAccountByUsername(username: String): DataUserAccount {
        delay(3000)
        return accountsList.first { it.login == username }
    }

    suspend fun loginUser(username: String, password: String): Result<DataUserAccount> {
        delay(3000)
        val account =
            accountsList.firstOrNull { it.login == username && password == "_${it.login}" }
        return if (account != null) {
            Result.success(account)
        } else {
            Result.failure(LoginFailedException())
        }
    }


    companion object {
        val accountsList = listOf<DataUserAccount>(
            DataUserAccount(
                login = "pasichnyi",
                name = "Oleksandr",
                surname = "Pasichnyi",
                isMaster = false,
            ),
            DataUserAccount(
                login = "borzova",
                name = "Yeseniia",
                surname = "Borzova",
                isMaster = false,
            ),
            DataUserAccount(
                login = "somemaster",
                name = "Master",
                isMaster = true,
            ),
        )
    }
}