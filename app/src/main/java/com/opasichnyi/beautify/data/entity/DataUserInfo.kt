package com.opasichnyi.beautify.data.entity

import com.google.gson.annotations.SerializedName

data class DataUserInfo(
    val username: String,
    @SerializedName("completed_orders")
    val completedOrders: Int? = null,
    @SerializedName("created_account")
    val createdAccount: String,
    @SerializedName("average_rating")
    val averageRating: Double? = null,
)