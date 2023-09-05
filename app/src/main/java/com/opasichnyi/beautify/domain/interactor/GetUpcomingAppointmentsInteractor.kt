package com.opasichnyi.beautify.domain.interactor

import com.opasichnyi.beautify.domain.repository.AppointmentsRepository

class GetUpcomingAppointmentsInteractor(
    private val appointmentsRepository: AppointmentsRepository,
) {

    suspend operator fun invoke() = appointmentsRepository.getUpcomingAppointments()
}