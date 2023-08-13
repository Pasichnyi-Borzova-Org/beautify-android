package com.opasichnyi.beautify.data.repository.impl

import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.mock.MockAccountDataSource
import com.opasichnyi.beautify.data.mapper.DataUserToDomainMapper
import com.opasichnyi.beautify.data.repository.iface.UserRepository
import com.opasichnyi.beautify.domain.entity.UserAccount

class UserRepositoryImpl(
    private val loggedInUserDatasource: LoggedInUserDatasource,
    private val accountDataSource: MockAccountDataSource,
    private val dataUserToDomainMapper: DataUserToDomainMapper,
) : UserRepository {

    override suspend fun getLoggedInUser(): UserAccount? {
        val currentLoggedUsername = loggedInUserDatasource.getLoggedInUser()
        return if (currentLoggedUsername != null) {
            val user = accountDataSource.getAccountByUsername(currentLoggedUsername)
            dataUserToDomainMapper.mapDataUserToDomain(user)
        } else {
            null
        }
    }

    override suspend fun loginUser(username: String, password: String): Result<UserAccount> {
        val userResult = accountDataSource.loginUser(username, password)
        if (userResult.isSuccess) {
            loggedInUserDatasource.saveLoggedInUser(username)
        }
        return userResult.map { dataUserToDomainMapper.mapDataUserToDomain(it) }
    }

    override suspend fun logoutUser() {
        loggedInUserDatasource.deleteLoggedInUser()
    }


}