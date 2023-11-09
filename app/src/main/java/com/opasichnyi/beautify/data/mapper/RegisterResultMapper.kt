package com.opasichnyi.beautify.data.mapper

import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataUserAccount
import com.opasichnyi.beautify.domain.entity.RegisterResult
import com.opasichnyi.beautify.domain.entity.RegisterError

class RegisterResultMapper(
    private val userAccountMapper: DataUserAccountToDomainMapper
) {

    fun mapRegisterResultToData(registerResult: RegisterResult): ApiCallResult<DataUserAccount, RegisterError> =
        when (registerResult) {
            is RegisterResult.Error -> ApiCallResult(null, registerResult.reason)
            is RegisterResult.Success -> ApiCallResult(
                userAccountMapper.mapDomainUserAccountToData(
                    registerResult.user
                ), null
            )
        }


    fun mapDataRegisterResultToDomain(registerResult: ApiCallResult<DataUserAccount, RegisterError>): RegisterResult =
        if (registerResult.data != null && registerResult.error == null) {
            RegisterResult.Success(userAccountMapper.mapDataUserAccountToDomain(registerResult.data))
        } else if (registerResult.data == null && registerResult.error != null) {
            RegisterResult.Error(registerResult.error)
        } else {
            throw Exception()
        }
}