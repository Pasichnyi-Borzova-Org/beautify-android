package com.opasichnyi.beautify.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {

    private val _progressStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val progressStateFlow: StateFlow<Boolean> = _progressStateFlow.asStateFlow()

    private val _errorStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorStateFlow: StateFlow<String?> = _errorStateFlow.asStateFlow()

    val coroutineContext = Dispatchers.Main +
            CoroutineExceptionHandler { _, exception ->
                showError(exception.message ?: "Something went wrong")
            } + SupervisorJob()


    fun showError(errorMessage: String) {
        _errorStateFlow.value = errorMessage
    }

    fun hideError() {
        _errorStateFlow.value = null
    }

    fun showProgress() {
        _progressStateFlow.value = true
    }

    fun hideProgress() {
        _progressStateFlow.value = false
    }

    val scope get() = viewModelScope
}