package com.opasichnyi.beautify.domain.entity

import java.util.Date

data class UserInfo(
    val userAccount: UserAccount,
    val completedOrders: Int? = null,
    val createdAccount: Date,
    val averageRating: Double? = null,
)