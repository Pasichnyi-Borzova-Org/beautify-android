package com.opasichnyi.beautify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lenovo.smartoffice.common.util.extension.lifecycle.repeatOnStart
import com.opasichnyi.beautify.databinding.FragmentAppointmentsListBinding
import com.opasichnyi.beautify.presentation.viewmodel.AppointmentsViewModel
import com.opasichnyi.beautify.ui.adapter.appointments.AppointmentsListAdapter
import com.opasichnyi.beautify.ui.base.BaseFragment
import com.opasichnyi.beautify.util.ext.navigateActionSafe


class AppointmentsListFragment :
    BaseFragment<FragmentAppointmentsListBinding, AppointmentsViewModel>() {

    private lateinit var adapter: AppointmentsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = AppointmentsListAdapter(viewModel::onAppointmentSelected, requireContext())
        binding?.appointmentsRecyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.loadAppointments(true)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun listenViewModel(
        viewModel: AppointmentsViewModel,
        binding: FragmentAppointmentsListBinding
    ) {
        super.listenViewModel(viewModel, binding)

        repeatOnStart {
            viewModel.appointmentsFlow.collect { list ->
                adapter.submitList(list)
                binding.appointmentsRecyclerView.adapter = adapter
            }
        }

        repeatOnStart {
            viewModel.selectedAppointmentFlow.collect { appointment ->
                val action =
                    AppointmentsListFragmentDirections.actionAppointmentsFragmentToAppointmentDetailsFragment(
                        appointment
                    )
                findNavController().navigateActionSafe(action)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAppointments()
    }
}