package com.opasichnyi.beautify.ui.adapter.appointments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.AppointmentListItemBinding
import com.opasichnyi.beautify.domain.entity.UserRole
import com.opasichnyi.beautify.presentation.entity.UIAppointment
import com.opasichnyi.beautify.ui.adapter.base.BaseListAdapter

class AppointmentsListAdapter(
    private val onOpenAppointmentInfo: (item: UIAppointment) -> Unit,
    private val context: Context,
) : BaseListAdapter<UIAppointment, AppointmentsListAdapter.AppointmentsViewHolder>(
    AppointmentsItemCallback(),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val binding = AppointmentListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentsViewHolder(binding, onOpenAppointmentInfo, context)
    }

    class AppointmentsViewHolder(
        private val binding: AppointmentListItemBinding,
        private val onOpenAppointmentDetails: (item: UIAppointment) -> Unit,
        private val context: Context,
    ) : BaseViewHolder<UIAppointment>(binding) {

        init {
            binding.root.setOnClickListener {
                item?.let { item -> this.onOpenAppointmentDetails(item) }
            }
        }

        override fun bind(item: UIAppointment) {
            super.bind(item)

            binding.apply {
                if (item.loggedInUserRole == UserRole.MASTER) {
                    priceTv.setTextColor(context.getColor(R.color.positive_green))
                }
                titleTv.text = item.title
                appointmentTimeTv.text = context.getString(
                    R.string.appointment_date_time_format,
                    item.date,
                    item.startTime,
                    item.endTime
                )
                partnerNameTv.text = item.partnerUserName
                priceTv.text = item.price
            }
        }
    }
}
