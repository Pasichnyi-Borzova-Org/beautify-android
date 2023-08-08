package com.opasichnyi.beautify.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAccount(
    val login: String,
    val name: String,
    val surname: String?,
    val isMaster: Boolean,
) : Parcelable