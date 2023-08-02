package com.opasichnyi.beautify.util.system

import android.content.Context
import android.content.Intent
import androidx.annotation.CheckResult

object MiuiUtils {

    private const val MIUI_APP_PERMISSION_ACTION = "miui.intent.action.APP_PERM_EDITOR"
    private const val MIUI_PERMISSION_PACKAGE = "com.miui.securitycenter"

    private const val PERMISSIONS_EDITOR_ACTIVITY_CLASS_NAME =
        "com.miui.permcenter.permissions.PermissionsEditorActivity"
    private const val APP_PERMISSIONS_EDITOR_ACTIVITY_CLASS_NAME =
        "com.miui.permcenter.permissions.AppPermissionsEditorActivity"

    private const val PACKAGE_NAME_EXTRA_KEY = "extra_pkgname"

    @CheckResult fun createMiui8AndHigherIntent(context: Context) =
        createMiuiIntent(context, PERMISSIONS_EDITOR_ACTIVITY_CLASS_NAME)

    @CheckResult fun createMiuiLessThan8Intent(context: Context) =
        createMiuiIntent(context, APP_PERMISSIONS_EDITOR_ACTIVITY_CLASS_NAME)

    @CheckResult private fun createMiuiIntent(
        context: Context,
        permissionsActivityClassName: String,
    ): Intent =
        Intent(MIUI_APP_PERMISSION_ACTION)
            .setClassName(MIUI_PERMISSION_PACKAGE, permissionsActivityClassName)
            .putExtra(PACKAGE_NAME_EXTRA_KEY, context.packageName)
}
