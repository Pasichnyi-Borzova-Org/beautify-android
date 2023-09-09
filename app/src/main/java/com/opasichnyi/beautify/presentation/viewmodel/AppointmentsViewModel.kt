package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.interactor.GetUpcomingAppointmentsInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AppointmentsViewModel(
    private val getUpcomingAppointmentsInteractor: GetUpcomingAppointmentsInteractor,
    private val appointmentMapper: DomainAppointmentToUIAppointmentMapper,
) : BaseViewModel() {

    private val _upcomingAppointments = MutableSharedFlow<List<UIAppointment>>()
    val upcomingAppointmentsFlow: SharedFlow<List<UIAppointment>> =
        _upcomingAppointments.asSharedFlow()

    private val _selectedAppointmentFlow = MutableSharedFlow<Appointment>()
    val selectedAppointmentFlow: SharedFlow<Appointment> =
        _selectedAppointmentFlow.asSharedFlow()

    private var appointments: List<Appointment> = emptyList()

    fun loadAppointments() {
        showProgress()
        scope.launch {
            _upcomingAppointments.emit(
                getUpcomingAppointmentsInteractor()
                    .also { appointments = it }
                    .map(
                        appointmentMapper::mapDomainAppointmentToUI
                    )
            )
            hideProgress()
        }
    }

    fun onAppointmentSelected(appointment: UIAppointment) = scope.launch {
        _selectedAppointmentFlow.emit(appointments.first { it.id == appointment.id })
    }
}