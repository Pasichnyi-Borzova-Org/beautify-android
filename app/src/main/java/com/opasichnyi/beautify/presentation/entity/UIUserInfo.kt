package com.opasichnyi.beautify.presentation.entity

import com.opasichnyi.beautify.domain.entity.UserAccount

data class UIUserInfo(
    val userAccount: UserAccount,
    val completedOrders: String,
    val experience: String,
    val averageRating: String,
)