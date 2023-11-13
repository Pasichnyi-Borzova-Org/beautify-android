package com.opasichnyi.beautify.data.datasource.remote

import android.util.Log
import com.google.gson.Gson
import com.opasichnyi.beautify.data.datasource.remote.service.AppointmentService
import com.opasichnyi.beautify.data.entity.DataAppointment

// TODO("Add security mechanisms for sensitive information manipulation")
class RemoteAppointmentsDataSource(
    private val appointmentService: AppointmentService,
) {

    suspend fun getAppointmentsOfUser(username: String): List<DataAppointment> {
        val result = appointmentService.getAppointmentsOfUser(username = username)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            // TODO("Wrap with result")
            emptyList()
        }
    }

    suspend fun tryCreateAppointment(appointment: DataAppointment): Boolean {
        val json = Gson().toJsonTree(appointment)
        val result = appointmentService.createAppointment(json = json)
        return result.isSuccessful
    }

    suspend fun deleteAppointment(appointment: DataAppointment): Boolean {
        val result = appointmentService.deleteAppointment(appointment.id)
        return result.isSuccessful
    }
}