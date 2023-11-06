package com.opasichnyi.beautify.data.datasource.mock

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.domain.mapper.DateMapper
import kotlinx.coroutines.delay
import java.lang.reflect.Type

class MockAppointmentsDataSource(
    context: Context,
    private val dateMapper: DateMapper,
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
        delay(2_000)
        val appointments = sharedPreferences.getString(APPOINTMENTS_TAG, null)
        val list = gson.fromJson<MutableList<DataAppointment>?>(
            appointments,
            APPOINTMENTS_LIST_TYPE
        ) ?: appointmentsList
        return list.filter { it.client.login == userName || it.master.login == userName }
    }

    // TODO("Return Data result instead of boolean")
    suspend fun tryCreateAppointment(appointment: DataAppointment): Boolean {
        delay(1_000)
        return if (!isTimeOccupied(appointment)) {
            saveAppointment(appointment)
            true
        } else {
            false
        }
    }

    private fun saveAppointment(appointment: DataAppointment) {
        saveAppointments(getAllAppointments() + appointment)
    }

    private fun isTimeOccupied(appointment: DataAppointment): Boolean {

        return getAllAppointments().any {
            appointmentsTimeIntersect(appointment, it)
        }
    }

    private fun appointmentsTimeIntersect(
        appointment1: DataAppointment,
        appointment2: DataAppointment,
    ): Boolean {
        val start1 = dateMapper.mapStringToDate(appointment1.startTime)
        val end1 = dateMapper.mapStringToDate(appointment1.endTime)
        val start2 = dateMapper.mapStringToDate(appointment2.startTime)
        val end2 = dateMapper.mapStringToDate(appointment2.endTime)
        return (start1 > start2 && start1 < end2) || (start2 > start1 && start2 < end1)
    }

    private fun getAllAppointments(): List<DataAppointment> {
        val appointments = sharedPreferences.getString(APPOINTMENTS_TAG, null)
        return gson.fromJson<MutableList<DataAppointment>?>(
            appointments,
            APPOINTMENTS_LIST_TYPE
        ) ?: appointmentsList
    }

    private fun saveAppointments(appointments: List<DataAppointment>) {
        val listString = gson.toJson(appointments)
        sharedPreferences.edit {
            putString(APPOINTMENTS_TAG, listString)
        }
    }

    companion object {

        const val APPOINTMENTS_TAG = "APPOINTMENTS_USERS"
        private val APPOINTMENTS_LIST_TYPE: Type =
            object : TypeToken<MutableList<DataAppointment>?>() {}.type

        val appointmentsList = mutableListOf(
            DataAppointment(
                id = 1,
                title = "Yasya 1",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userBorzova,
                startTime = "2023-11-16T12:00:00Z",
                endTime = "2023-11-16T12:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 2,
                title = "Sasha 1",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userPasichnyi,
                startTime = "2023-11-14T10:00:00Z",
                endTime = "2023-11-14T11:00:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 3,
                title = "Sasha 2",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userPasichnyi,
                startTime = "2023-11-15T13:00:00Z",
                endTime = "2023-11-15T14:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 4,
                title = "Yasya 2",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userBorzova,
                startTime = "2023-11-16T16:00:00Z",
                endTime = "2023-11-16T18:00:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 5,
                title = "Yasya 3",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userBorzova,
                startTime = "2023-11-16T15:00:00Z",
                endTime = "2023-11-16T15:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
            DataAppointment(
                id = 6,
                title = "Sasha 2",
                master = MockAccountDataSource.userMaster,
                client = MockAccountDataSource.userPasichnyi,
                startTime = "2023-11-04T13:00:00Z",
                endTime = "2023-11-04T14:30:00Z",
                price = 3.50,
                description = "test description 1"
            ),
        )
    }
}