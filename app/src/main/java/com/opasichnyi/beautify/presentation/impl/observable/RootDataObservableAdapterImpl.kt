package com.opasichnyi.beautify.presentation.impl.observable

import android.content.res.Resources
import androidx.annotation.AnyThread
import com.opasichnyi.beautify.presentation.base.observable.WriteRootDataObservable
import com.opasichnyi.beautify.presentation.base.observable.adapter.RootDataObservableAdapter
import com.opasichnyi.beautify.util.extension.flow.MutableStateFlowEvent
import com.opasichnyi.beautify.util.extension.flow.tryEmitEvent
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity.NavIcon
import timber.log.Timber
import java.util.Collections
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class RootDataObservableAdapterImpl @Inject constructor(
    private val rootDataObservable: WriteRootDataObservable,
    private val resourcesProvider: Provider<Resources>,
) : RootDataObservableAdapter,
    WriteRootDataObservable by rootDataObservable {

    private val dataForInvalidateCurrentScreenMap = Collections.synchronizedMap(
        RootDataUIEntity::class
            .sealedSubclasses
            .associateWith<KClass<out RootDataUIEntity>, RootDataUIEntity?> { null },
    )

    override val resources: Resources get() = resourcesProvider.get()

    override val navigationIconEnableEvent = MutableStateFlowEvent<Unit>()

    @AnyThread override fun emit(newData: RootDataUIEntity): Boolean {
        dataForInvalidateCurrentScreenMap[newData::class] = newData
        return rootDataObservable.emit(newData)
    }

    @AnyThread override fun invalidateRootData(key: KClass<out RootDataUIEntity>?): Boolean =
        when (key) {
            NavIcon::class -> navigationIconEnableEvent.tryEmitEvent(Unit)
            null -> dataForInvalidateCurrentScreenMap.keys.map(::invalidateRootData).all { it }

            else -> {
                val data = dataForInvalidateCurrentScreenMap[key]

                if (data == null) {
                    Timber.v("Cached value is null by key=$key")
                    false
                } else {
                    rootDataObservable.emit(data)
                }
            }
        }
}
