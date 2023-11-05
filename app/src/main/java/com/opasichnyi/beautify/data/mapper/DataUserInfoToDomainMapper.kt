package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataUserInfo
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserInfo

class DataUserInfoToDomainMapper(
    private val dateMapper: DateMapper,
) {

    fun mapDataUserInfoToDomain(userInfo: DataUserInfo, userAccount: UserAccount): UserInfo {
        return UserInfo(
            userAccount = userAccount,
            averageRating = userInfo.averageRating,
            createdAccount = dateMapper.mapStringToDate(userInfo.createdAccount),
            completedOrders = userInfo.completedOrders,
        )
    }
}