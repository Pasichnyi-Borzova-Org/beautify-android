package com.opasichnyi.beautify.data.datasource.remote

import com.google.gson.Gson
import com.opasichnyi.beautify.data.datasource.remote.service.AppointmentService
import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationError

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

    suspend fun tryCreateAppointment(appointment: DataAppointment): ApiCallResult<DataAppointment, AppointmentCreationError> {
        val json = Gson().toJsonTree(appointment)
        val response = appointmentService.createAppointment(json = json)
        return response.body()!!
    }

    suspend fun deleteAppointment(appointment: DataAppointment): Boolean {
        val result = appointmentService.deleteAppointment(appointment.id)
        return result.isSuccessful
    }

    suspend fun completeAppointment(appointment: DataAppointment): DataAppointment {
        val result = appointmentService.completeAppointment(appointment.id)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            throw Exception("Error completing appointment")
        }
    }

    suspend fun rateAppointment(appointment: DataAppointment, rating: Int): DataAppointment {
        val result = appointmentService.rateAppointment(appointment.id, rating)
        return if (result.isSuccessful) {
            result.body()!!
        } else {
            throw Exception("Error rating appointment")
        }
    }

    suspend fun tryUpdateAppointment(appointment: DataAppointment): DataAppointment {
        val json = Gson().toJsonTree(appointment)
        val result = appointmentService.updateAppointment(json = json)
        if (result.isSuccessful) {
            return result.body()!!
        } else {
            throw Exception("Error updating appointment")
        }
    }
}