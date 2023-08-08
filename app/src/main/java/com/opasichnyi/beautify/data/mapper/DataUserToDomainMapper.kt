package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.domain.entity.UserAccount

class DataUserToDomainMapper {

    fun mapDataUserToDomain(dataUserAccount: DataUserAccount) : UserAccount =
        UserAccount(
            login = dataUserAccount.login,
            name = dataUserAccount.name,
            surname = dataUserAccount.surname,
            isMaster = dataUserAccount.isMaster,
        )

    fun mapDomainUserToData(userAccount: UserAccount) : DataUserAccount =
        DataUserAccount(
            login = userAccount.login,
            name = userAccount.name,
            surname = userAccount.surname,
            isMaster = userAccount.isMaster
        )
}