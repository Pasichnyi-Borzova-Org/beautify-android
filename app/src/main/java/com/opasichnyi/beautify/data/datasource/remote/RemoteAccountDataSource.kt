package com.opasichnyi.beautify.data.datasource.remote

import android.util.Log
import com.google.gson.Gson
import com.opasichnyi.beautify.data.datasource.remote.service.AccountService
import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataRegisterData
import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.data.entity.DataUserInfo
import com.opasichnyi.beautify.domain.entity.RegisterError

// TODO("Add security mechanisms for sensitive information manipulation")
class RemoteAccountDataSource(
    private val accountService: AccountService,
) {

    suspend fun loginUser(username: String, password: String): Result<DataUserAccount> {
        val json = Gson().toJsonTree(mapOf("username" to username, "password" to password))
        val result = accountService.loginUser(json)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            // TODO("Change exception")
            Result.failure(Exception())
        }
    }

    suspend fun getAllUsers(): List<DataUserAccount> {
        val response = accountService.getAllUsers()
        Log.e("DataSource", "isSuccess ${response.isSuccessful}")
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            // TODO("Change by wrapping in Result entity")
            emptyList()
        }
    }

    suspend fun getAccountByUsername(username: String): DataUserAccount {
        val response = accountService.getAccountByUsername(username)
        Log.e("DataSource", "isSuccess ${response.isSuccessful}")
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            // TODO("Change by wrapping in Result entity")
            throw Exception()
        }
    }

    suspend fun getUserInfo(username: String): DataUserInfo {
        val response = accountService.getUserInfo(username)
        Log.e("DataSource", "isSuccess ${response.isSuccessful}")
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            // TODO("Change by wrapping in Result entity")
            throw Exception()
        }
    }

    suspend fun registerUser(registerData: DataRegisterData): ApiCallResult<DataUserAccount, RegisterError> {
        val json = Gson().toJsonTree(registerData)
        val response = accountService.registerUser(json)
        return response.body()!!
    }

    suspend fun deleteUser(username: String) {
        accountService.deleteUser(username)
    }
}