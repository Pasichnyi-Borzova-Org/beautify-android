package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

class EditAppointmentInteractor(
    private val appointmentsRepository: AppointmentsRepository,
) {

    // TODO("It's not OK to use CreationResult after update")
    suspend operator fun invoke(appointment: Appointment): AppointmentCreationResult =
        appointmentsRepository.updateAppointment(appointment)
}