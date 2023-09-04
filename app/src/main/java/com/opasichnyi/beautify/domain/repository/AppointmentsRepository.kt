package com.opasichnyi.beautify.domain.repository

import com.opasichnyi.beautify.domain.entity.Appointment

interface AppointmentsRepository {

    suspend fun getUpcomingAppointments(): List<Appointment>
}