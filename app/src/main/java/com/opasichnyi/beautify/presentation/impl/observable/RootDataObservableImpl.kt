package com.opasichnyi.beautify.presentation.impl.observable

import androidx.annotation.AnyThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.opasichnyi.beautify.presentation.base.observable.WriteRootDataObservable
import com.opasichnyi.beautify.util.extension.flow.launchCollectWithLifecycleOwner
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.reflect.KClass

class RootDataObservableImpl @Inject constructor() : WriteRootDataObservable {

    /**
     * [Map] of all [RootDataUIEntity].
     * You don't need to change anything to add your child to a sealed class.
     */
    @VisibleForTesting
    val rootDataMap: Map<KClass<out RootDataUIEntity>, RootDataValue<RootDataUIEntity>> =
        RootDataUIEntity::class
            .sealedSubclasses
            .asSequence()
            .map { it to RootDataValue<RootDataUIEntity>() }
            .toMap()

    @AnyThread override fun launchCollect(
        owner: LifecycleOwner,
        minActiveState: Lifecycle.State,
        collector: (RootDataUIEntity) -> Unit,
    ) {
        rootDataMap.values.forEach {
            it.launchCollect(
                owner = owner,
                minActiveState = minActiveState,
                collector = collector,
            )
        }
    }

    @AnyThread override fun emit(newData: RootDataUIEntity): Boolean {
        val rootDataValue = rootDataMap[newData::class]
            ?: throw IllegalArgumentException("Key ${newData::class} not found")
        val oldData = rootDataValue.oldData

        return if (newData == oldData) {
            Timber.v("Data $newData already exists and equals to $oldData")
            false
        } else {
            rootDataValue.oldData = newData
            true
        }
    }

    @Suppress("UNCHECKED_CAST")
    @AnyThread
    override fun <T : RootDataUIEntity> get(key: KClass<out T>): T? =
        rootDataMap[key]?.oldData as T?

    /**
     * A class that encapsulates a simple and thread-safe wrapper over data.
     */
    @Suppress("DataClassContainsFunctions")
    @VisibleForTesting
    data class RootDataValue<T : RootDataUIEntity>(
        @get:VisibleForTesting val stateFlow: MutableStateFlow<T?> = MutableStateFlow(null),
        private val _oldData: AtomicReference<T?> = AtomicReference(stateFlow.value),
    ) {

        /**
         * This value is different from [stateFlow] ([MutableStateFlow.value]),
         * because it gives the result immediately.
         * [MutableStateFlow] updates the value ([MutableStateFlow.value])
         * after a post operation ([androidx.arch.core.executor.ArchTaskExecutor.postToMainThread])
         * on [MutableStateFlow.emit].
         */
        var oldData: T?
            get() = _oldData.get()
            set(value) {
                _oldData.set(value)
                stateFlow.value = value
            }

        @AnyThread fun launchCollect(
            owner: LifecycleOwner,
            minActiveState: Lifecycle.State,
            collector: (T) -> Unit,
        ) {
            stateFlow.launchCollectWithLifecycleOwner(
                lifecycleOwner = owner,
                minActiveState = minActiveState,
                collector = { it?.let(collector) },
            )
        }
    }
}
