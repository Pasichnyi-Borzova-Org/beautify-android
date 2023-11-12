package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

class DeleteAppointmentInteractor(
    private val appointmentsRepository: AppointmentsRepository,
) {

    suspend operator fun invoke(appointment: Appointment) {
        appointmentsRepository.deleteAppointment(appointment)
    }
}