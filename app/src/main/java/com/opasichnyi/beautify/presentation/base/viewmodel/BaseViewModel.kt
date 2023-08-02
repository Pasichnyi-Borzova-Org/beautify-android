package com.opasichnyi.beautify.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opasichnyi.beautify.presentation.base.CoroutineExecutor
import com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate
import com.opasichnyi.beautify.presentation.base.observable.adapter.RootDataObservableAdapter
import com.opasichnyi.beautify.util.extension.flow.MutableStateFlowEvent

abstract class BaseViewModel(
    presentationData: PresentationDataDelegate,
) : ViewModel(),
    PresentationDataDelegate by presentationData,
    RootDataObservableAdapter by presentationData.rootDataObservable,
    CoroutineExecutor {

    override val scope get() = viewModelScope

    override val unexpectedErrorEvent = MutableStateFlowEvent<Unit>()
}
