package com.opasichnyi.beautify.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent

open class BaseViewModel : ViewModel() {

    val scope get() = viewModelScope

    val unexpectedErrorLiveEvent = LiveEvent<Throwable>()
}