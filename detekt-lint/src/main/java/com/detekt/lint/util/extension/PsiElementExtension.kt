package com.detekt.lint.util.extension

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClass

fun PsiElement.parentClass(): KtClass? =
    when (val parent = parent) {
        null -> null
        is KtClass -> parent
        else -> parent.parentClass()
    }
