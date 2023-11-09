package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.domain.entity.UserAccount
import com.opasichnyi.beautify.domain.entity.UserRole

class DataUserAccountToDomainMapper {

    fun mapDomainUserAccountToData(userAccount: UserAccount) =
        DataUserAccount(
            username = userAccount.username,
            name = userAccount.name,
            surname = userAccount.surname,
            city = userAccount.city,
            isMaster = when (userAccount.role) {
                UserRole.MASTER -> 1
                UserRole.CLIENT -> 0
            },
        )


    fun mapDataUserAccountToDomain(userAccount: DataUserAccount) =
        UserAccount(
            username = userAccount.username,
            name = userAccount.name,
            surname = userAccount.surname,
            city = userAccount.city,
            role = if (userAccount.isMaster == 1) UserRole.MASTER else UserRole.CLIENT,
        )

}