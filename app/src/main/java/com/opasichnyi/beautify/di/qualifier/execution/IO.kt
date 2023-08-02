package com.opasichnyi.beautify.di.qualifier.execution

import javax.inject.Qualifier

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
)
@Qualifier
@MustBeDocumented
annotation class IO
