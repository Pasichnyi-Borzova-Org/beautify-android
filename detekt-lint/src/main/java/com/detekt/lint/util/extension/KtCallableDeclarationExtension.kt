package com.detekt.lint.util.extension

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets

fun KtCallableDeclaration.getDeclarationDescriptorParameterSequence(
    bindingContext: BindingContext,
): Sequence<DeclarationDescriptor> =
    valueParameters.asSequence().map { ktParameter ->
        val typeElement = ktParameter.typeReference?.typeElement as? KtUserType?
        val referenceExpression = typeElement?.referenceExpression ?: return@map emptyList()
        referenceExpression.getReferenceTargets(bindingContext)
    }.flatten()
