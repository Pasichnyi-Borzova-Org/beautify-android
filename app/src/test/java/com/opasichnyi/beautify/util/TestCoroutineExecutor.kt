package com.opasichnyi.beautify.util

import com.opasichnyi.beautify.presentation.base.CoroutineExecutor
import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class TestCoroutineExecutor(
    override val scope: CoroutineScope,
    override val backgroundDispatcher: CoroutineDispatcher,
    override val coroutineExceptionHandler: CoroutineExceptionHandler,
    override val unexpectedErrorEvent: MutableStateFlow<UIEvent<Unit>>,
) : CoroutineExecutor
