package com.opasichnyi.beautify.data.entity

data class DataUserInfo(
    val username: String,
    val completedOrders: Int? = null,
    val createdAccount: String,
    val averageRating: Double? = null,
)