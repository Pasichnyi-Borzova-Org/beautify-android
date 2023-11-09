package com.opasichnyi.beautify.domain.repository

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult

interface AppointmentsRepository {

    suspend fun getUpcomingAppointments(): List<Appointment>

    suspend fun tryAddAppointment(appointment: Appointment): AppointmentCreationResult
}