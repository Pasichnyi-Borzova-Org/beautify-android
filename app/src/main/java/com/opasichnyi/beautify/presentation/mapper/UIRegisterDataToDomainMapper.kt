package com.opasichnyi.beautify.presentation.mapper

import com.opasichnyi.beautify.domain.entity.RegisterData
import com.opasichnyi.beautify.presentation.entity.UIRegisterData

class UIRegisterDataToDomainMapper {

    fun mapUIRegisterDataToDomain(uiRegisterData: UIRegisterData): RegisterData =
        RegisterData(
            username = uiRegisterData.login,
            name = uiRegisterData.name,
            surname = uiRegisterData.surname,
            city = uiRegisterData.city,
            password = uiRegisterData.password,
            role = uiRegisterData.role,
        )

}