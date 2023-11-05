package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.UserInfo
import com.opasichnyi.beautify.presentation.entity.UIUserInfo
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

class DomainUserInfoToUIUserInfoMapper {

    fun mapDomainUserInfoToUI(userInfo: UserInfo): UIUserInfo {


        return UIUserInfo(
            userAccount = userInfo.userAccount,
            averageRating = userInfo.averageRating?.toString() ?: "-",
            completedOrders = userInfo.completedOrders?.toString() ?: "-",
            experience = getExpString(userInfo.createdAccount),
        )
    }

    // TODO("Add localizations")
    private fun getExpString(registeredDate: Date): String {
        val today = LocalDate.now()
        val createdAt = registeredDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        val period = Period.between(createdAt, today)
        return if (period.years > 0) {
            period.years.toString() + if (period.years == 1) {
                " year"
            } else {
                " years"
            }
        } else {
            "< year"
        }
    }
}