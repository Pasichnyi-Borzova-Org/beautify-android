package com.detekt.lint.util.extension

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtPureClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets

fun KtPureClassOrObject.getSuperClassOrInterfaceSequence(
    bindingContext: BindingContext,
): Sequence<DeclarationDescriptor> =
    superTypeListEntries.asSequence().mapNotNull { superTypeClass ->
        val typeAsUserType = superTypeClass.typeAsUserType
        val referenceExpression = typeAsUserType?.referenceExpression ?: return@mapNotNull null
        referenceExpression.getReferenceTargets(bindingContext)
    }.flatten()

fun KtPureClassOrObject.getAllSuperClassOrInterfaceSequence(
    bindingContext: BindingContext,
): Sequence<DeclarationDescriptor> {
    val superClassOrInterfaceSequence = getSuperClassOrInterfaceSequence(bindingContext)
    return superClassOrInterfaceSequence
        .map { it.getAllSuperClassOrInterfaceSequence() }
        .flatten()
        .plus(superClassOrInterfaceSequence)
}
