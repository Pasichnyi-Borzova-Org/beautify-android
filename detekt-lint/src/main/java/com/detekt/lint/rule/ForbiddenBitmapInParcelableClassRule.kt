package com.detekt.lint.rule

import com.detekt.lint.util.extension.getAllSuperClassOrInterfaceSequence
import com.detekt.lint.util.extension.getDeclarationDescriptorParameterSequence
import com.detekt.lint.util.extension.getPackageAndClassFullName
import com.detekt.lint.util.extension.parentClass
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.internal.RequiresTypeResolution
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtSecondaryConstructor

@RequiresTypeResolution
class ForbiddenBitmapInParcelableClassRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "ForbiddenBitmapInParcelableClass",
        severity = Severity.Defect,
        description = "See TransactionTooLargeException",
        debt = Debt.FIVE_MINS,
    )

    override fun visitPrimaryConstructor(constructor: KtPrimaryConstructor) {
        super.visitPrimaryConstructor(constructor)
        visitConstructor(constructor)
    }

    override fun visitSecondaryConstructor(constructor: KtSecondaryConstructor) {
        super.visitSecondaryConstructor(constructor)
        visitConstructor(constructor)
    }

    private fun <T : KtConstructor<T>> visitConstructor(constructor: KtConstructor<T>) {
        val clazz = constructor.parentClass() ?: return

        val hasBitmapParameter =
            constructor.getDeclarationDescriptorParameterSequence(bindingContext)
                .map(DeclarationDescriptor::getPackageAndClassFullName)
                .any { it == "android.graphics.Bitmap" }

        if (hasBitmapParameter) {
            val hasParcelableParent = clazz
                .getAllSuperClassOrInterfaceSequence(bindingContext)
                .map(DeclarationDescriptor::getPackageAndClassFullName)
                .any { it == "android.os.Parcelable" }

            if (hasParcelableParent) {
                report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(clazz),
                        message = "WARNING: java.lang.RuntimeException: " +
                            "android.os.TransactionTooLargeException: data parcel size $$$ bytes",
                    ),
                )
            }
        }
    }
}
