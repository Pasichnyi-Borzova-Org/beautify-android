package com.opasichnyi.beautify.data.datasource.mock

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opasichnyi.beautify.data.entity.DataUserInfo
import com.opasichnyi.beautify.data.exception.LoginFailedException
import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserRole
import kotlinx.coroutines.delay
import java.lang.reflect.Type

class MockAccountDataSource(
    context: Context,
) {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    suspend fun getAccountByUsername(username: String): UserAccount {
        delay(3000)
        return getAllUsers().first { it.login == username }
    }

    suspend fun loginUser(username: String, password: String): Result<UserAccount> {
        delay(3000)
        val account =
            getAllUsers().firstOrNull { it.login == username && password == "_${it.login}" }
        return if (account != null) {
            Result.success(account)
        } else {
            Result.failure(LoginFailedException())
        }
    }

    suspend fun registerUser(registerData: RegisterData): RegisterResult {
        delay(3000)
        val currentList = getAllUsers()
        return if (currentList.any { it.login == registerData.login }) {
            RegisterResult.Error
        } else {
            val newAccount = UserAccount(
                login = registerData.login,
                name = registerData.login,
                surname = registerData.login,
                // TODO("Add city to the registration screen")
                null,
                role = registerData.role
            )
            saveUsers(currentList + newAccount)
            RegisterResult.Success(newAccount)
        }
    }

    fun getAllUsers(): List<UserAccount> {
        val registeredUsers = sharedPreferences.getString(REGISTERED_USERS_TAG, null)
        return gson.fromJson<MutableList<UserAccount>?>(registeredUsers, USERS_LIST_TYPE)
            ?: accountsList
    }

    fun getUserInfo(username: String): DataUserInfo {
        return userInfoList.first { it.username == username }
    }

    private fun saveUsers(list: List<UserAccount>) {
        val listString = gson.toJson(list)
        sharedPreferences.edit {
            putString(REGISTERED_USERS_TAG, listString)
        }
    }

    companion object {

        const val REGISTERED_USERS_TAG = "REGISTERED_USERS"

        private val USERS_LIST_TYPE: Type = object : TypeToken<MutableList<UserAccount>?>() {}.type

        val userPasichnyi = UserAccount(
            login = "pasichnyi",
            name = "Oleksandr",
            surname = "Pasichnyi",
            city = "Kharkiv",
            role = UserRole.CLIENT,
        )

        val userBorzova = UserAccount(
            login = "borzova",
            name = "Yeseniia",
            surname = "Borzova",
            city = "Kharkiv",
            role = UserRole.CLIENT,
        )

        val userMaster = UserAccount(
            login = "somemaster",
            name = "Master",
            surname = "Kekus",
            city = "Kyiv",
            role = UserRole.MASTER,
        )

        private val accountsList = mutableListOf(
            userPasichnyi,
            userBorzova,
            userMaster,
        )

        private val infoPasichnyi = DataUserInfo(
            username = "pasichnyi",
            createdAccount = "2023-04-14T10:00:00Z",
        )

        private val infoBorzova = DataUserInfo(
            username = "borzova",
            createdAccount = "2022-05-14T10:00:00Z",
        )

        private val infoMaster = DataUserInfo(
            username = "somemaster",
            completedOrders = 340,
            createdAccount = "2021-07-23T10:00:00Z",
            averageRating = 4.6,
        )

        private val userInfoList = mutableListOf(
            infoPasichnyi,
            infoBorzova,
            infoMaster,
        )
    }
}