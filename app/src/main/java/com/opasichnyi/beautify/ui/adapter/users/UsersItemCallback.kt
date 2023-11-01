package com.opasichnyi.beautify.ui.adapter.users

import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.ui.adapter.base.BaseItemCallback

class UsersItemCallback : BaseItemCallback<UserAccount>() {

    override fun areItemsTheSame(
        oldItem: UserAccount,
        newItem: UserAccount
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: UserAccount,
        newItem: UserAccount
    ): Boolean {
        return oldItem == newItem
    }
}