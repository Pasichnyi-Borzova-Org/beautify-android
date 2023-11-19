package com.opasichnyi.beautify.ui.fragment

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentAppointmentDetailsBinding
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentStatus
import com.opasichnyi.beautify.domain.entity.UserRole
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentDetailsViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment

class AppointmentDetailsFragment :
    BaseFragment<FragmentAppointmentDetailsBinding, AppointmentDetailsViewModel>() {

    private val args: AppointmentDetailsFragmentArgs by navArgs()

    private val appointment: Appointment get() = args.appointment

    override fun onResume() {
        super.onResume()
        viewModel.onAppointmentLoaded(appointment)

        setupButtons()
    }

    override fun listenViewModel(
        viewModel: AppointmentDetailsViewModel,
        binding: FragmentAppointmentDetailsBinding
    ) {
        super.listenViewModel(viewModel, binding)
        repeatOnStart {
            viewModel.selectedAppointmentFlow.collect { appointment ->
                setupAppointmentInfo(appointment, binding)
            }
        }

        repeatOnStart {
            viewModel.deletedAppointmentFlow.collect {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupButtons() {
        binding?.deleteAppointmentButton?.setOnClickListener {
            showConfirmationDialog(
                message = getString(R.string.delete_appointment_confirmation),
                onSubmit = { viewModel.deleteAppointment(appointment) })

        }
        binding?.editAppointmentButton?.setOnClickListener {
            TODO("Not implemented yet")
        }
        binding?.completeAppointmentButton?.setOnClickListener {
            showConfirmationDialog(
                message = getString(R.string.complete_appointment_confirmation),
                onSubmit = { viewModel.completeAppointment(appointment) })
        }
        binding?.rateButton?.setOnClickListener {
            TODO("Not implemented yet")
        }
    }

    private fun setupAppointmentInfo(
        appointment: UIAppointment,
        binding: FragmentAppointmentDetailsBinding
    ) {
        Log.e("Fragment", "$appointment")
        binding.apply {
            titleTv.text = appointment.title
            descriptionTv.text = appointment.description
            userTv.text = appointment.partnerUserName
            timeTv.text =
                getString(
                    R.string.appointment_date_time_format,
                    appointment.date,
                    appointment.startTime,
                    appointment.endTime
                )
            when (appointment.status) {
                AppointmentStatus.CANNOT_COMPLETE -> {
                    deleteAppointmentButton.visibility = View.VISIBLE
                    editAppointmentButton.visibility = View.VISIBLE
                }

                AppointmentStatus.CAN_COMPLETE -> {
                    deleteAppointmentButton.visibility = View.VISIBLE
                    editAppointmentButton.visibility = View.VISIBLE
                    if (appointment.loggedInUserRole == UserRole.MASTER) {
                        completeAppointmentButton.visibility = View.VISIBLE
                    }
                }

                AppointmentStatus.COMPLETED -> {
                    completeAppointmentButton.visibility = View.INVISIBLE
                    editAppointmentButton.visibility = View.INVISIBLE
                    deleteAppointmentButton.visibility = View.INVISIBLE
                    appointmentCompletedTextView.visibility = View.VISIBLE
                    if (appointment.loggedInUserRole == UserRole.CLIENT) {
                        rateButton.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}