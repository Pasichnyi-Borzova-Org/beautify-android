package com.opasichnyi.beautify.ui.fragment

import android.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
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
import com.opasichnyi.beautify.util.ext.navigateActionSafe


class AppointmentDetailsFragment :
    BaseFragment<FragmentAppointmentDetailsBinding, AppointmentDetailsViewModel>() {

    private val args: AppointmentDetailsFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        viewModel.onAppointmentLoaded(args.appointment)

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

        repeatOnStart {
            viewModel.editAppointmentFlow.collect {
                editAppointment(it)
            }
        }
    }

    private fun setupButtons() {
        binding?.deleteAppointmentButton?.setOnClickListener {
            showConfirmationDialog(
                message = getString(R.string.delete_appointment_confirmation),
                onSubmit = { viewModel.deleteCurrentAppointment() })

        }
        binding?.editAppointmentButton?.setOnClickListener {
            viewModel.editCurrentAppointment()
        }
        binding?.completeAppointmentButton?.setOnClickListener {
            showConfirmationDialog(
                message = getString(R.string.complete_appointment_confirmation),
                onSubmit = { viewModel.completeCurrentAppointment() })
        }
        binding?.rateButton?.setOnClickListener {
            showDialog()
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
                    if (appointment.loggedInUserRole == UserRole.CLIENT && appointment.rating == null) {
                        rateButton.visibility = View.VISIBLE
                    } else {
                        rateButton.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun showDialog() {
        val popDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val linearLayout = LinearLayout(requireContext())
        linearLayout.setHorizontalGravity(Gravity.CENTER)
        val rating = RatingBar(requireContext())
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        rating.layoutParams = lp
        rating.numStars = 5
        rating.stepSize = 1f

        linearLayout.addView(rating)
        popDialog.setTitle(R.string.rate_appointment)

        popDialog.setView(linearLayout)
        rating.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, v, b -> println("Rated val:$v") }

        popDialog.setPositiveButton(
            android.R.string.ok
        ) { _, _ ->
            viewModel.rateAppointment(rating.progress)
        }
            .setNegativeButton(
                R.string.cancel
            ) { dialog, _ -> dialog.cancel() }
        popDialog.create()
        popDialog.show()
    }

    private fun editAppointment(appointment: Appointment) {
        val action =
            AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToCreateAppointmentFragment(
                appointment.master,
                appointment
            )
        findNavController().navigateActionSafe(action)
    }
}