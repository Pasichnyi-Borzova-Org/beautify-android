package com.opasichnyi.beautify.data.entity

data class DataUserAccount (
    val login: String,
    val name: String,
    val surname: String? = null,
    val isMaster: Boolean,
)