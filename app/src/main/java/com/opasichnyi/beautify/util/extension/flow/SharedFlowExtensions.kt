package com.opasichnyi.beautify.util.extension.flow

import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T> MutableSharedFlow<UIEvent<T>>.emitEvent(value: T) {
    emit(UIEvent(value))
}

suspend fun <T> MutableSharedFlow<UIEvent<T>>.emitEvent(error: Throwable) {
    emit(UIEvent(error))
}

fun <T> MutableSharedFlow<UIEvent<T>>.tryEmitEvent(value: T): Boolean =
    tryEmit(UIEvent(value))

fun <T> MutableSharedFlow<UIEvent<T>>.tryEmitEvent(error: Throwable): Boolean =
    tryEmit(UIEvent(error))

@Suppress("FunctionName")
fun <T> MutableStateFlowEvent(event: UIEvent<T> = UIEvent()): MutableStateFlow<UIEvent<T>> =
    MutableStateFlow(event)
