package com.opasichnyi.beautify.data.repository.impl

import android.util.Log
import com.opasichnyi.beautify.data.datasource.LoggedInUserDatasource
import com.opasichnyi.beautify.data.datasource.remote.RemoteAccountDataSource
import com.opasichnyi.beautify.data.mapper.DataRegisterDataToDomainMapper
import com.opasichnyi.beautify.data.mapper.DataUserAccountToDomainMapper
import com.opasichnyi.beautify.data.mapper.DataUserInfoToDomainMapper
import com.opasichnyi.beautify.data.mapper.RegisterResultMapper
import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserInfo
import com.opasichnyi.beautify.domain.repository.UserRepository

class UserRepositoryImpl(
    private val loggedInUserDatasource: LoggedInUserDatasource,
    private val userInfoMapper: DataUserInfoToDomainMapper,
    private val accountDataSource: RemoteAccountDataSource,
    private val userAccountMapper: DataUserAccountToDomainMapper,
    private val registerResultMapper: RegisterResultMapper,
    private val registerDataToDomainMapper: DataRegisterDataToDomainMapper,
) : UserRepository {

    override suspend fun getLoggedInUser(): UserAccount? {
        val currentLoggedUsername = loggedInUserDatasource.getLoggedInUser()
        return if (currentLoggedUsername != null) {
            userAccountMapper.mapDataUserAccountToDomain(
                accountDataSource.getAccountByUsername(
                    currentLoggedUsername
                )
            )
        } else {
            null
        }
    }

    override suspend fun loginUser(username: String, password: String): Result<UserAccount> {
        val userResult = accountDataSource.loginUser(username, password)
        if (userResult.isSuccess) {
            loggedInUserDatasource.saveLoggedInUser(username)
        }
        return userResult.map { userAccountMapper.mapDataUserAccountToDomain(it) }
    }

    override suspend fun logoutUser() =
        loggedInUserDatasource.deleteLoggedInUser()

    override suspend fun registerUser(registerData: RegisterData): RegisterResult =
        registerResultMapper.mapDataRegisterResultToDomain(
            accountDataSource.registerUser(
                registerDataToDomainMapper.mapDomainRegisterDataToData(registerData)
            )
        ).also {
            Log.e("UserRepositoryImpl", "$it")
            if (it is RegisterResult.Success) {
                loggedInUserDatasource.saveLoggedInUser(registerData.username)
            }
        }

    override suspend fun getAllUsers(): List<UserAccount> =
        accountDataSource.getAllUsers().map { userAccountMapper.mapDataUserAccountToDomain(it) }

    override suspend fun getUserInfo(userAccount: UserAccount): UserInfo {
        return userInfoMapper.mapDataUserInfoToDomain(
            userInfo = accountDataSource.getUserInfo(userAccount.username),
            userAccount = userAccount
        )
    }
}