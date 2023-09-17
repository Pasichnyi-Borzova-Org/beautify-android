package com.opasichnyi.beautify.ui.adapter.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.databinding.UserListItemBinding
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.ui.adapter.base.BaseListAdapter

class UsersListAdapter(
    private val onOpenAppointmentInfo: (item: UserAccount) -> Unit,
    private val context: Context,
) : BaseListAdapter<UserAccount, UsersListAdapter.UsersViewHolder>(
    UsersItemCallback(),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UserListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsersViewHolder(binding, onOpenAppointmentInfo, context)
    }

    class UsersViewHolder(
        private val binding: UserListItemBinding,
        private val onOpenUserDetails: (item: UserAccount) -> Unit,
        private val context: Context,
    ) : BaseViewHolder<UserAccount>(binding) {

        init {
            binding.root.setOnClickListener {
                item?.let { item -> this.onOpenUserDetails(item) }
            }
        }

        override fun bind(item: UserAccount) {
            super.bind(item)

            binding.apply {
                nameSurname.text = context.getString(
                    R.string.name_surname_format,
                    item.name,
                    item.surname,
                )
            }
        }
    }
}