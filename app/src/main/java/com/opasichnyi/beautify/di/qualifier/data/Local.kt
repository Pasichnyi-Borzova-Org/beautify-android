package com.opasichnyi.beautify.di.qualifier.data

import javax.inject.Qualifier

/**
 * Qualifier for data sources that allows to inject [Local] data source implementation
 */
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
)
@Qualifier
@MustBeDocumented
annotation class Local
