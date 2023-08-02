package com.opasichnyi.beautify.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.opasichnyi.beautify.presentation.base.CoroutineExecutor
import com.opasichnyi.beautify.presentation.impl.entity.UIEvent
import com.opasichnyi.beautify.util.Callbacks
import com.opasichnyi.beautify.util.TestConfig
import com.opasichnyi.beautify.util.TestCoroutineExceptionHandler
import com.opasichnyi.beautify.util.TestCoroutineExecutor
import com.opasichnyi.beautify.util.extension.flow.MutableStateFlowEvent
import com.opasichnyi.beautify.util.extension.flow.getErrorOrNull
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.bouncycastle.util.test.SimpleTestResult
import org.bouncycastle.util.test.TestFailedException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.coroutines.CoroutineContext

/* ktlint-disable max-line-length */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = TestConfig.MANIFEST, sdk = [TestConfig.SDK_VERSION])
class CoroutineExecutorTest : CoroutineScope {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testScheduler = TestCoroutineScheduler()

    private val testDispatcher = StandardTestDispatcher(testScheduler)

    override val coroutineContext = testDispatcher + CoroutineName("CoroutineExecutorTest")

    private lateinit var executorScope: CoroutineScope

    private lateinit var executor: CoroutineExecutor

    private val testCoroutineExceptionHandler = TestCoroutineExceptionHandler()

    private lateinit var unexpectedErrorStateFlow: MutableStateFlow<UIEvent<Unit>>

    @MockK private lateinit var callbacks: Callbacks

    private val throwable = RuntimeException()

    private fun assertIfHasUncaughtException() {
        testCoroutineExceptionHandler.uncaughtExceptions.apply {
            assertEquals(size, 1)
            assertEquals(first(), throwable)
        }
    }

    private fun assertIfNoExceptions() {
        assertTrue(testCoroutineExceptionHandler.uncaughtExceptions.isEmpty())
        assertNull(unexpectedErrorStateFlow.getErrorOrNull())
    }

    private suspend fun callBlockAndCancel() {
        callbacks.block()
        executorScope.cancel()
        awaitCancellation()
    }

    private fun defaultLaunchSafe(
        context: CoroutineContext = testDispatcher,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        startImmediately: Boolean = true,
        onCancellation: (e: CancellationException) -> Unit = { callbacks.onCancellation() },
        onFailure: (t: Throwable) -> Unit = callbacks::onFailure,
        finally: (() -> Unit) = callbacks::finally,
        block: suspend CoroutineScope.() -> Unit = { callbacks.block() },
    ): Job {
        val job = executor.launchSafe(
            context = context,
            start = start,
            onCancellation = onCancellation,
            onFailure = onFailure,
            finally = finally,
            block = block,
        )

        if (startImmediately) {
            testScheduler.runCurrent()
        }

        return job
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        unexpectedErrorStateFlow = MutableStateFlowEvent()

        executorScope =
            CoroutineScope(SupervisorJob() + testDispatcher + CoroutineName("executorScope"))

        executor = TestCoroutineExecutor(
            scope = executorScope,
            backgroundDispatcher = testDispatcher,
            coroutineExceptionHandler = testCoroutineExceptionHandler,
            unexpectedErrorEvent = unexpectedErrorStateFlow,
        )
    }

    @Test
    fun `test launchSafe normal execution without exceptions`() {
        val job = defaultLaunchSafe()

        verifySequence {
            callbacks.block()
            callbacks.finally()
        }

        assertIfNoExceptions()
        assertTrue(job.isCompleted)
    }

    @Test
    fun `test that launchSafe throws Exception when block() and onFailure() throws exception`() {
        defaultLaunchSafe(
            onFailure = { t ->
                callbacks.onFailure(t)
                throw throwable
            },
        ) {
            callbacks.block()
            throw TestFailedException(SimpleTestResult(true, "Any error"))
        }

        verifySequence {
            callbacks.block()
            callbacks.onFailure(any())
            callbacks.finally()
        }

        assertIfHasUncaughtException()
    }

    @Test
    fun `test that launchSafe calls onCancellation() when cancelled from block()`() {
        defaultLaunchSafe {
            callBlockAndCancel()
        }

        verifySequence {
            callbacks.block()
            callbacks.onCancellation()
            callbacks.finally()
        }
    }

    @Test
    fun `test that launchSafe not start coroutine when cancelled and onCancellation() start launchSafe`() {
        defaultLaunchSafe(
            onCancellation = {
                callbacks.onCancellation()
                executor.launchSafe {
                    callbacks.block()
                }
            },
        ) {
            callBlockAndCancel()
        }

        verify(exactly = 1) { callbacks.block() }
    }

    @Test
    fun `test that launchSafe not start coroutine when cancelled and finally() start launchSafe`() {
        defaultLaunchSafe(
            finally = {
                callbacks.finally()
                executor.launchSafe {
                    callbacks.block()
                }
            },
        ) {
            callBlockAndCancel()
        }

        verify(exactly = 1) { callbacks.block() }
    }

    @Test
    fun `test that launchSafe throws Exception when cancelled and onCancellation() throws exception`() {
        defaultLaunchSafe(
            onCancellation = {
                callbacks.onCancellation()
                throw throwable
            },
        ) {
            callBlockAndCancel()
        }

        verifySequence {
            callbacks.block()
            callbacks.onCancellation()
            callbacks.finally()
        }

        assertIfHasUncaughtException()
    }

    @Test
    fun `test that onFailure() is called if block() throws Exception`() {
        defaultLaunchSafe {
            callbacks.block()
            throw throwable
        }

        verifySequence {
            callbacks.block()
            callbacks.onFailure(throwable)
            callbacks.finally()
        }

        assertTrue(testCoroutineExceptionHandler.uncaughtExceptions.isEmpty())
    }

    @Test
    fun `test that launchSafe throws exception when finally throws exception`() {
        defaultLaunchSafe(
            finally = {
                callbacks.finally()
                throw throwable
            },
        )

        verifySequence {
            callbacks.block()
            callbacks.finally()
        }

        assertIfHasUncaughtException()
    }

    @Test
    fun `test that unexpectedErrorLiveEvent emits throwable when block() throws Exception`() {
        executor.launchSafe(
            onCancellation = { callbacks.onCancellation() },
            finally = callbacks::finally,
        ) {
            callbacks.block()
            throw throwable
        }

        testScheduler.runCurrent()

        assertTrue(testCoroutineExceptionHandler.uncaughtExceptions.isEmpty())
        assertEquals(unexpectedErrorStateFlow.getErrorOrNull(), throwable)
    }

    @Test
    fun `test before cancel`() {
        executor.launchSafe {
            callbacks.block()
        }

        assertTrue(executorScope.coroutineContext.job.children.any())
        val launchSafeJob = executorScope.coroutineContext.job.children.first()
        launchSafeJob.cancel()

        verify(exactly = 0) { callbacks.block() }

        testScheduler.runCurrent()

        verify(exactly = 0) { callbacks.block() }
    }

    @Test
    fun `test that execution doesn't start when LAZY coroutine start is set`() {
        executor.launchSafe(
            start = CoroutineStart.LAZY,
        ) {
            callbacks.block()
        }

        verify(exactly = 0) { callbacks.block() }

        val nonActiveJob = executorScope.coroutineContext.job.children.firstOrNull { !it.isActive }
        assertNotNull(nonActiveJob)
        nonActiveJob?.start()

        testScheduler.runCurrent()

        verify(exactly = 1) { callbacks.block() }
    }

    @Test
    fun `test that coroutine is launched with passed context`() {
        val coroutineName = CoroutineName("CustomCoroutineContext")

        executor.launchSafe(
            context = executorScope.coroutineContext + coroutineName,
        ) {
            val job = (executorScope.coroutineContext.job.children.first() as CoroutineScope)
            assertEquals(job.coroutineContext[CoroutineName.Key], coroutineName)
            callbacks.block()
        }

        testScheduler.runCurrent()

        assertIfNoExceptions()
    }

    @Test
    fun `test that launchSafe executes block with its coroutineContext when no context passed`() {
        val coroutineName = executorScope.coroutineContext[CoroutineName.Key]

        executor.launchSafe {
            val job = (executorScope.coroutineContext.job.children.first() as CoroutineScope)
            assertEquals(job.coroutineContext[CoroutineName.Key], coroutineName)
            callbacks.block()
        }

        testScheduler.runCurrent()

        assertIfNoExceptions()
    }

    @Test
    fun `test that coroutineContext executes in background`() {
        val startThread = Thread.currentThread()

        lateinit var workerThread: Thread

        val job = executor.launchSafe(
            context = Dispatchers.Default,
        ) {
            workerThread = Thread.currentThread()
        }

        runTest { job.join() }

        assertFalse(startThread.id == workerThread.id)
    }

    @Test
    fun `test that ATOMIC coroutine launches even if cancelled before launch`() {
        executor.launchSafe(
            start = CoroutineStart.ATOMIC,
        ) {
            callbacks.block()
        }

        assertTrue(executorScope.coroutineContext.job.children.count() > 0)
        val launchSafeJob = executorScope.coroutineContext.job.children.first()
        launchSafeJob.cancel()

        verify(exactly = 0) { callbacks.block() }

        testScheduler.runCurrent()

        verify(exactly = 1) { callbacks.block() }
    }

    @Test
    fun `test UNDISPATCHED switches to passed context after suspending`() {
        val mutex = Mutex()
        val job = executor.launchSafe(
            context = Dispatchers.Default,
            start = CoroutineStart.UNDISPATCHED,
        ) {
            println(Thread.currentThread().name)
            assertTrue(Thread.currentThread().name.contains("main", true))
            mutex.lock()
            mutex.withLock {
                println(Thread.currentThread().name)
                assertTrue(Thread.currentThread().name.contains("default", true))
            }
        }

        runTest {
            awaitAll(
                async { job.join() },
                async { mutex.unlock() },
            )
        }

        assertIfNoExceptions()
    }

    @Test
    fun `test UNDISPATCHED precancel`() {
        val mutex = Mutex()
        executor.launchSafe(
            start = CoroutineStart.UNDISPATCHED,
        ) {
            mutex.lock()
            mutex.withLock {
                callbacks.block()
                assertTrue(false)
            }
        }

        verify(exactly = 0) { callbacks.block() }

        executorScope.cancel()
        mutex.unlock()

        verify(exactly = 0) { callbacks.block() }
    }
}
