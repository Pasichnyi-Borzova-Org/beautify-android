package com.opasichnyi.beautify.presentation.viewmodel

import com.opasichnyi.beautify.domain.interactor.RegistrationInteractor
import com.opasichnyi.beautify.presentation.base.BaseViewModel
import com.opasichnyi.beautify.presentation.entity.UIRegisterData
import com.opasichnyi.beautify.presentation.entity.UIRegisterResult
import com.opasichnyi.beautify.presentation.mapper.DomainRegistrationResultToUIMapper
import com.opasichnyi.beautify.presentation.mapper.UIRegisterDataToDomainMapper
import com.opasichnyi.beautify.presentation.util.UIRegisterDataValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registrationInteractor: RegistrationInteractor,
    private val uiRegisterDataToDomainMapper: UIRegisterDataToDomainMapper,
    private val domainRegistrationResultToUIMapper: DomainRegistrationResultToUIMapper,
) : BaseViewModel() {

    private val _registrationResult: MutableSharedFlow<UIRegisterResult> = MutableSharedFlow()
    val registrationResult: SharedFlow<UIRegisterResult> = _registrationResult.asSharedFlow()

    fun tryRegister(uiRegisterData: UIRegisterData) = scope.launch {
        showProgress()
        val validationResult = UIRegisterDataValidator().validate(uiRegisterData)
        if (!validationResult.isAllValid()) {
            _registrationResult.emit(UIRegisterResult.Error(validationResult))
            hideProgress()
        } else {
            val registrationResult =
                domainRegistrationResultToUIMapper.mapRegistrationResultToUiRegistrationResult(
                    registrationInteractor(
                        uiRegisterDataToDomainMapper.mapUIRegisterDataToDomain(uiRegisterData)
                    ),
                    validationResult
                )
            _registrationResult.emit(registrationResult)
            hideProgress()
        }
    }
}