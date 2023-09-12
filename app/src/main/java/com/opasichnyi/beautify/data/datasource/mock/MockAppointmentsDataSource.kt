package com.opasichnyi.beautify.data.datasource.mock

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opasichnyi.beautify.data.entity.DataAppointment
import kotlinx.coroutines.delay
import java.lang.reflect.Type

class MockAppointmentsDataSource(
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

    suspend fun getAppointmentsOfUser(userName: String): List<DataAppointment> {
        delay(3_000)
        val appointments = sharedPreferences.getString(APPOINTMENTS_TAG, null)
        val list = gson.fromJson<MutableList<DataAppointment>?>(
            appointments,
            APPOINTMENTS_LIST_TYPE
        ) ?: appointmentsList
        return list.filter { it.clientUserName == userName || it.masterUserName == userName }
    }

    companion object {

        const val APPOINTMENTS_TAG = "APPOINTMENTS_USERS"
        private val APPOINTMENTS_LIST_TYPE: Type =
            object : TypeToken<MutableList<DataAppointment>?>() {}.type

        val appointmentsList = mutableListOf(
            DataAppointment(
                id = 1,
                title = "Yasya 1",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userBorzova.login,
                startTime = "2023-09-16T12:00:00Z",
                endTime = "2023-09-16T12:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 2,
                title = "Sasha 1",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userPasichnyi.login,
                startTime = "2023-09-14T10:00:00Z",
                endTime = "2023-09-14T11:00:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 3,
                title = "Sasha 2",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userPasichnyi.login,
                startTime = "2023-09-15T13:00:00Z",
                endTime = "2023-09-15T14:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 4,
                title = "Yasya 2",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userBorzova.login,
                startTime = "2023-09-16T16:00:00Z",
                endTime = "2023-09-16T18:00:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 5,
                title = "Yasya 3",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userBorzova.login,
                startTime = "2023-09-16T15:00:00Z",
                endTime = "2023-09-16T15:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 6,
                title = "Sasha 2",
                masterUserName = MockAccountDataSource.userMaster.login,
                clientUserName = MockAccountDataSource.userPasichnyi.login,
                startTime = "2023-09-04T13:00:00Z",
                endTime = "2023-09-04T14:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
        )
    }
}