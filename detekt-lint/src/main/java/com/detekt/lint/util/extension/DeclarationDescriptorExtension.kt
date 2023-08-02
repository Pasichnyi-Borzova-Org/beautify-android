package com.detekt.lint.util.extension

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.impl.ClassConstructorDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.ClassDescriptorBase
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.getAllSuperclassesWithoutAny
import org.jetbrains.kotlin.resolve.descriptorUtil.getSuperInterfaces
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedClassDescriptor

fun DeclarationDescriptor.getSuperClassOrInterfaceSequence(): Sequence<DeclarationDescriptor> =
    when (this) {
        // interface
        is ClassDescriptorBase -> {
            getSuperInterfaces().asSequence()
        }

        // class
        is ClassConstructorDescriptorImpl -> {
            containingDeclaration.let {
                it.getAllSuperclassesWithoutAny().asSequence() + it.getSuperInterfaces()
            }
        }

        else -> emptySequence()
    }

fun DeclarationDescriptor.getAllSuperClassOrInterfaceSequence(): Sequence<DeclarationDescriptor> =
    getSuperClassOrInterfaceSequence()
        .map { it.getAllSuperClassOrInterfaceSequence() + it }
        .flatten()

fun DeclarationDescriptor.getPackageAndClassFullName(): String? =
    when (this) {
        is ClassDescriptorBase -> "${containingDeclaration.fqNameSafe.asString()}.$name"
        is ClassConstructorDescriptorImpl -> fqNameSafe.parentOrNull()?.asString()
        is DeserializedClassDescriptor -> fqNameSafe.asString()
        else -> null
    }
