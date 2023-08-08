package com.opasichnyi.beautify.presentation.base

import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

open class BaseViewModel : ViewModel() {

    val unexpectedErrorLiveEvent = LiveEvent<Throwable>()
}