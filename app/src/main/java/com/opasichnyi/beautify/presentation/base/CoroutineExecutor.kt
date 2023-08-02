package com.opasichnyi.beautify.presentation.base

import com.opasichnyi.beautify.presentation.base.entity.PresentationDataDelegate
import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import com.opasichnyi.beautify.util.extension.flow.emitEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface CoroutineExecutor {

    /**
     * That [CoroutineScope] must include [SupervisorJob][kotlinx.coroutines.SupervisorJob] or
     * [Job][kotlinx.coroutines.Job].
     */
    val scope: CoroutineScope

    /**
     * @see [PresentationDataDelegate.backgroundDispatcher]
     */
    val backgroundDispatcher: CoroutineDispatcher

    /**
     * @see [PresentationDataDelegate.coroutineExceptionHandler]
     */
    val coroutineExceptionHandler: CoroutineExceptionHandler

    /**
     * For push Exception from coroutines
     *
     * ```
     * Q: Why don`t we use SharedFlow?
     * A: If SharedFlow emits one event it does not buffers if there are no collectors.
     * Such events are not listened to in the dead lifecycle.
     * Channel.BUFFERED only works on two or more events.
     * ```
     *
     * Problem: https://lightrun.com/answers/kotlin-kotlinx-coroutines-sharedflow-that-emits-once-but-buffers-if-there-are-no-collectors
     *
     * Original issue: https://github.com/Kotlin/kotlinx.coroutines/issues/3002
     *
     * ```
     * Q: Why don't we use suggested solutions from the community?
     * A: Community solutions are not final, stable or tested.
     * The best solution is the good old solution from Google - UI events.
     * ```
     *
     * Google solution: https://developer.android.com/topic/architecture/ui-layer/events
     *
     * @see UIEvent
     */
    val unexpectedErrorEvent: MutableStateFlow<UIEvent<Unit>>

    /**
     * The function ensures safe launch of coroutines.
     *
     * @param context additional to [CoroutineScope.coroutineContext] context of the coroutine.
     * The default value is [backgroundDispatcher].
     *
     * @param start coroutine start option. The default value is [CoroutineStart.DEFAULT].
     *
     * @param onCancellation Invoking the given action after cancels to be launched.
     * The default value [onCancellation].
     *
     * @param onFailure Catches exceptions in the flow completion and calls
     * a specified action with the caught exception. The default value [onFailure].
     *
     * @param finally Invoking the given action after the launch is completed, caught or canceled.
     * You can use this fun to hide progress dialogs.
     *
     * @param block The coroutine code which will be invoked in the context of the provided scope.
     *
     * @return [Job] for [Job.invokeOnCompletion] or [Job.cancel]
     */
    fun launchSafe(
        context: CoroutineContext = backgroundDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onCancellation: (e: CancellationException) -> Unit = ::onCancellation,
        onFailure: suspend (t: Throwable) -> Unit = this@CoroutineExecutor::onFailure,
        finally: suspend () -> Unit = { },
        block: suspend CoroutineScope.() -> Unit,
    ): Job =
        scope.launchSafe(
            context = context,
            start = start,
            onCancellation = onCancellation,
            onFailure = onFailure,
            finally = finally,
            block = block,
        )

    /**
     * The function ensures safe launch of coroutines.
     *
     * @param context additional to [CoroutineScope.coroutineContext] context of the coroutine.
     * The default value is [backgroundDispatcher].
     *
     * @param start coroutine start option. The default value is [CoroutineStart.DEFAULT].
     *
     * @param onCancellation Invoking the given action after cancels to be launched.
     * The default value [onCancellation].
     *
     * @param onFailure Catches exceptions in the flow completion and calls
     * a specified action with the caught exception. The default value [onFailure].
     *
     * @param finally Invoking the given action after the launch is completed, caught or canceled.
     * You can use this fun to hide progress dialogs.
     *
     * @param block The coroutine code which will be invoked in the context of the provided scope.
     *
     * @return [Job] for [Job.invokeOnCompletion] or [Job.cancel]
     */
    @Suppress("TooGenericExceptionCaught")
    fun CoroutineScope.launchSafe(
        context: CoroutineContext = backgroundDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onCancellation: (e: CancellationException) -> Unit = ::onCancellation,
        onFailure: suspend (t: Throwable) -> Unit = this@CoroutineExecutor::onFailure,
        finally: suspend () -> Unit = { },
        block: suspend CoroutineScope.() -> Unit,
    ): Job =
        launch(context + coroutineExceptionHandler, start) {
            try {
                block()
            } catch (e: CancellationException) {
                onCancellation(e)
            } catch (t: Throwable) {
                onFailure(t)
            } finally {
                finally.invoke()
            }
        }

    /**
     * Use an override of this method to find top exceptions that concretize a problem.
     */
    suspend fun onFailure(t: Throwable) {
        unexpectedErrorEvent.emitEvent(t)
    }

    /**
     * You must check [scope][CoroutineScope].[isActive][kotlinx.coroutines.isActive]
     * before start new launch operation
     */
    fun onCancellation(e: CancellationException) {
        // none
    }
}
