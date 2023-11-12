package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.DataRegisterData
import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.domain.entity.UserRole

class DataRegisterDataToDomainMapper {

    fun mapDataRegisterDataToDomain(registerData: DataRegisterData) = RegisterData(
        username = registerData.username,
        name = registerData.name,
        surname = registerData.surname,
        city = registerData.city,
        password = registerData.password,
        role = if (registerData.isMaster == 0) UserRole.CLIENT else UserRole.MASTER
    )

    fun mapDomainRegisterDataToData(registerData: RegisterData) = DataRegisterData(
        username = registerData.username,
        name = registerData.name,
        surname = registerData.surname,
        city = registerData.city,
        password = registerData.password,
        isMaster = when (registerData.role) {
            UserRole.MASTER -> 1
            UserRole.CLIENT -> 0
        }
    )
}