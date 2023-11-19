package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.interactor.CompleteAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.DeleteAppointmentInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AppointmentDetailsViewModel(
    private val appointmentMapper: DomainAppointmentToUIAppointmentMapper,
    private val deleteAppointmentInteractor: DeleteAppointmentInteractor,
    private val completeAppointmentInteractor: CompleteAppointmentInteractor,
) : BaseViewModel() {

    private val _selectedAppointmentFlow = MutableSharedFlow<UIAppointment>()
    val selectedAppointmentFlow: SharedFlow<UIAppointment> =
        _selectedAppointmentFlow.asSharedFlow()

    private val _deletedAppointmentFlow = MutableSharedFlow<Appointment>()
    val deletedAppointmentFlow: SharedFlow<Appointment> =
        _deletedAppointmentFlow.asSharedFlow()

    fun onAppointmentLoaded(appointment: Appointment) = scope.launch {
        _selectedAppointmentFlow.emit(
            appointmentMapper.mapDomainAppointmentToUI(appointment)
        )
    }

    fun deleteAppointment(appointment: Appointment) = scope.launch {
        deleteAppointmentInteractor(appointment)
        _deletedAppointmentFlow.emit(appointment)
    }

    fun completeAppointment(appointment: Appointment) = scope.launch {
        _selectedAppointmentFlow.emit(
            appointmentMapper.mapDomainAppointmentToUI(
                completeAppointmentInteractor(appointment)
            )
        )
    }
}