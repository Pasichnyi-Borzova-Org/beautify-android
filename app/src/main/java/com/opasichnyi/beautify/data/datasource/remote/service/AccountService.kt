package com.opasichnyi.beautify.data.datasource.remote.service

import com.google.gson.JsonElement
import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.data.entity.DataUserInfo
import com.opasichnyi.beautify.domain.entity.RegisterError
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountService {

    @POST("users/login")
    suspend fun loginUser(@Body json: JsonElement): Response<DataUserAccount>

    @GET("users")
    suspend fun getAllUsers(): Response<List<DataUserAccount>>

    @GET("users/{username}/info")
    suspend fun getUserInfo(@Path("username") username: String): Response<DataUserInfo>

    @GET("users/{username}/account")
    suspend fun getAccountByUsername(@Path("username") username: String): Response<DataUserAccount>

    @POST("users")
    suspend fun registerUser(@Body json: JsonElement): Response<ApiCallResult<DataUserAccount, RegisterError>>

    // TODO("Not secure, use token or other mechanisms")
    @DELETE("users/delete/{username}")
    suspend fun deleteUser(@Path("username") username: String): Response<Boolean>
}