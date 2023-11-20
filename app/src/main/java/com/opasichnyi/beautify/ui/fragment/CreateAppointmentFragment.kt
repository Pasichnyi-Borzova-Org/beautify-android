package com.opasichnyi.beautify.ui.fragment

import android.text.InputFilter
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.FragmentCreateAppointmentBinding
import com.opasichnyi.beautify.domain.entity.Appointment
import com.opasichnyi.beautify.domain.entity.AppointmentStatus
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.presentation.filters.DecimalDigitsInputFilter
import com.opasichnyi.beautify.presentation.viewmodel.CreateAppointmentViewModel
import com.opasichnyi.beautify.ui.base.BaseFragment
import com.opasichnyi.beautify.util.ext.navigateActionSafe

class CreateAppointmentFragment :
    BaseFragment<FragmentCreateAppointmentBinding, CreateAppointmentViewModel>() {

    private val args: CreateAppointmentFragmentArgs by navArgs()

    private val master: UserAccount get() = args.selectedMaster

    private val editingAppointment: Appointment? get() = args.editingAppointment

    private lateinit var client: UserAccount

    override fun onResume() {
        super.onResume()
        initView()
        if (isEditing()) {
            editingAppointment?.let { viewModel.onUpdateScreenOpened(it) }
                ?: throw Exception("Marked as editing but no editing appointment found")
        } else {
            viewModel.onCreateScreenOpened(master)
        }
    }

    override fun listenViewModel(
        viewModel: CreateAppointmentViewModel,
        binding: FragmentCreateAppointmentBinding
    ) {
        super.listenViewModel(viewModel, binding)

        viewLifecycleOwner.repeatOnStart {
            viewModel.clientFlow.collect {
                client = it
                binding.clientText.text = getFullNameFromUserAccount(it)
            }
        }

        viewLifecycleOwner.repeatOnStart {
            viewModel.masterFlow.collect {
                binding.masterText.text = getFullNameFromUserAccount(it)
            }
        }

        viewLifecycleOwner.repeatOnStart {
            viewModel.createdAppointmentResultFlow.collect {
                showAppointmentDetailsPage(it)
            }
        }

        viewLifecycleOwner.repeatOnStart {
            viewModel.editingAppointmentFlow.collect {
                setEditingView(it)
            }
        }
    }

    private fun initView() {
        binding?.priceTextInput?.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(10, 2))

        binding?.createButton?.setOnClickListener {
            viewModel.tryCreateAppointment(
                getAppointmentFromFields()
            )
        }
    }

    private fun getFullNameFromUserAccount(account: UserAccount): String {
        return getString(
            R.string.name_surname_format,
            account.name,
            account.surname,
        )
    }

    private fun showAppointmentDetailsPage(appointment: Appointment) {
        val action =
            CreateAppointmentFragmentDirections.actionCreateAppointmentFragmentToAppointmentDetailsFragment(
                appointment
            )
        findNavController().navigateActionSafe(action)
    }

    private fun isEditing() = editingAppointment != null

    private fun setEditingView(appointment: UIAppointment) {
        client = appointment.client
        binding?.clientText?.text = getFullNameFromUserAccount(appointment.client)
        binding?.masterText?.text = getFullNameFromUserAccount(appointment.master)
        binding?.titleEditText?.setText(appointment.title)
        binding?.dateEditText?.setText(appointment.date)
        binding?.timeFromEditText?.setText(appointment.startTime)
        binding?.timeToEditText?.setText(appointment.endTime)
        binding?.descriptionEditText?.setText(appointment.description)

        binding?.changeMasterButton?.visibility = View.INVISIBLE
        binding?.changeClientButton?.visibility = View.INVISIBLE

        binding?.createButton?.apply {
            setText(R.string.edit_appointment)
            setOnClickListener {
                viewModel.tryEditAppointment(getAppointmentFromFields())
            }
        }
    }

    private fun getAppointmentFromFields() = UIAppointment(
        id = editingAppointment?.id ?: -1,
        title = binding?.titleEditText?.text.toString(),
        master = master,
        client = client,
        loggedInUserRole = client.role,
        date = binding?.dateEditText?.text.toString(),
        startTime = binding?.timeFromEditText?.text.toString(),
        endTime = binding?.timeToEditText?.text.toString(),
        price = "20",
        status = AppointmentStatus.CANNOT_COMPLETE,
        description = binding?.descriptionEditText?.text.toString(),
    )
}