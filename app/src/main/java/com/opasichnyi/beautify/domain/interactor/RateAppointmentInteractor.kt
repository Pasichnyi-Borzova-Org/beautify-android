package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

class RateAppointmentInteractor(
    private val appointmentsRepository: AppointmentsRepository,
) {

    suspend operator fun invoke(appointment: Appointment, rating: Int): Appointment =
        appointmentsRepository.rateAppointment(appointment, rating)
}