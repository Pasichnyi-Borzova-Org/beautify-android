package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationResult
import com.opasichnyi.beautify.domain.entity.ErrorReason
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.interactor.EditAppointmentInteractor
import com.opasichnyi.beautify.domain.interactor.LoggedInUserInteractor
import com.opasichnyi.beautify.domain.interactor.TryCreateAppointmentInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.mapper.DomainAppointmentToUIAppointmentMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreateAppointmentViewModel(
    private val loggedInUserInteractor: LoggedInUserInteractor,
    private val tryCreateAppointmentInteractor: TryCreateAppointmentInteractor,
    private val appointmentToUIAppointmentMapper: DomainAppointmentToUIAppointmentMapper,
    private val editAppointmentInteractor: EditAppointmentInteractor,
) : BaseViewModel() {

    private val _masterFlow = MutableSharedFlow<UserAccount>()
    val masterFlow: SharedFlow<UserAccount> =
        _masterFlow.asSharedFlow()

    private val _clientFlow = MutableSharedFlow<UserAccount>()
    val clientFlow: SharedFlow<UserAccount> =
        _clientFlow.asSharedFlow()

    private val _createdAppointmentResultFlow = MutableSharedFlow<Appointment>()
    val createdAppointmentResultFlow: SharedFlow<Appointment> =
        _createdAppointmentResultFlow.asSharedFlow()

    private val _editingAppointmentFlow = MutableSharedFlow<UIAppointment>()
    val editingAppointmentFlow: SharedFlow<UIAppointment> =
        _editingAppointmentFlow.asSharedFlow()

    fun onCreateScreenOpened(masterUserAccount: UserAccount) = scope.launch {
        showProgress()
        val clientUserAccount = loggedInUserInteractor()
        _masterFlow.emit(masterUserAccount)
        if (clientUserAccount != null) {
            _clientFlow.emit(clientUserAccount)
        }
        hideProgress()
    }

    fun onUpdateScreenOpened(appointment: Appointment) = scope.launch {
        _editingAppointmentFlow.emit(
            appointmentToUIAppointmentMapper.mapDomainAppointmentToUI(
                appointment
            )
        )
    }

    fun tryCreateAppointment(appointment: UIAppointment) =
        scope.launch {
            try {
                val result = tryCreateAppointmentInteractor(
                    appointmentToUIAppointmentMapper.mapUIAppointmentToDomain(
                        appointment
                    )
                )
                when (result) {
                    is AppointmentCreationResult.Success -> onAppointmentCreationSuccess(result)
                    is AppointmentCreationResult.Error -> onAppointmentCreationError(result)
                }
            } catch (ex: Exception) {
                showError(ex.message ?: "")
            }
        }

    fun tryEditAppointment(appointment: UIAppointment) = scope.launch {

        try {
            val result = editAppointmentInteractor(
                appointmentToUIAppointmentMapper.mapUIAppointmentToDomain(
                    appointment
                )
            )
            when (result) {
                is AppointmentCreationResult.Success -> onAppointmentCreationSuccess(result)
                is AppointmentCreationResult.Error -> onAppointmentCreationError(result)
            }
        } catch (ex: Exception) {
            showError(ex.message ?: "Unknown error")
        }
    }

    private fun onAppointmentCreationSuccess(result: AppointmentCreationResult.Success) =
        scope.launch {
            _createdAppointmentResultFlow.emit(result.appointment)
        }

    // TODO("Emit result and fully process on fragment")
    // TODO("BTF-34 process all possible backend errors")
    private fun onAppointmentCreationError(result: AppointmentCreationResult.Error) {
        showError(
            when (result.reason) {
                ErrorReason.TIME_BUSY -> "Time is busy. Select another one"
            }
        )
    }

}