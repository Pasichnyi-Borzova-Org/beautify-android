package com.opasichnyi.beautify.util.extension.flow

import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T> StateFlow<UIEvent<T>>.getValueOrNull(
    forceResult: Boolean = false,
): T? = value.getDataOrNull(forceResult)

fun <T> StateFlow<UIEvent<T>>.getErrorOrNull(
    forceResult: Boolean = false,
): Throwable? = value.getErrorOrNull(forceResult)

@Suppress("FunctionName")
fun <T> MutableStateFlowEvent(): MutableStateFlow<UIEvent<T>> = MutableStateFlow(UIEvent())
