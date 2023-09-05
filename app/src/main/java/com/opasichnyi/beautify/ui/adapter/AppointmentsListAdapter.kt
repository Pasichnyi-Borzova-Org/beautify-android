package com.opasichnyi.beautify.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.AppointmentListItemBinding
import com.opasichnyi.beautify.domain.entity.UserRole
import com.opasichnyi.beautify.presentation.entity.UIAppointment

class AppointmentsListAdapter(
    private val onOpenAppointmentInfo: (item: UIAppointment) -> Unit,
    private val context: Context,
) : BaseListAdapter<UIAppointment, AppointmentsListAdapter.CountryViewHolder>(
    AppointmentsItemCallback(),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = AppointmentListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding, onOpenAppointmentInfo, context)
    }

    class CountryViewHolder(
        private val binding: AppointmentListItemBinding,
        private val onOpenCountryInfo: (item: UIAppointment) -> Unit,
        private val context: Context,
    ) : BaseViewHolder<UIAppointment>(binding) {

        init {
            binding.root.setOnClickListener {
                item?.let { item -> this.onOpenCountryInfo(item) }
            }
        }

        override fun bind(item: UIAppointment) {
            super.bind(item)

            binding.apply {
                if (item.loggedInUserRole == UserRole.MASTER) {
                    priceTv.setTextColor(context.getColor(R.color.positive_green))
                }
                titleTv.text = item.title
                appointmentTimeTv.text = item.startTime
                partnerNameTv.text = item.partnerUsername
                priceTv.text = item.price
            }
        }
    }
}
