package com.opasichnyi.beautify.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class LoggedInUserDatasource(
    context: Context,
) {

    private var sharedPreferences: SharedPreferences

    init {
        val masterKey: MasterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveLoggedInUser(username: String) =
        sharedPreferences.edit {
            putString(LOGIN_USERNAME_TAG, username)
        }

    fun deleteLoggedInUser() =
        sharedPreferences.edit {
            remove(LOGIN_USERNAME_TAG)
        }

    fun getLoggedInUser(): String? =
        sharedPreferences.getString(LOGIN_USERNAME_TAG, null)

    companion object {

        const val LOGIN_USERNAME_TAG = "LOGIN_USERNAME"
    }

}