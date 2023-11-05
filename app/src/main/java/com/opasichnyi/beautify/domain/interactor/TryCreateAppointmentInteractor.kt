package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

class TryCreateAppointmentInteractor(
    private val appointmentsRepository: AppointmentsRepository,
) {

    suspend operator fun invoke(appointment: Appointment) : AppointmentCreationResult{
        return appointmentsRepository.tryAddAppointment(appointment)
    }
}