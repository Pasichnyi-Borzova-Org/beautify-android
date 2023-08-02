package com.opasichnyi.beautify.util.view.navigation

import androidx.activity.OnBackPressedCallback

/**
 * Callback class that perform some action on [androidx.activity.ComponentActivity.onBackPressed].
 *
 * @see OnBackPressedCallback
 *
 * @param enabled denote that is this callback enabled or not.
 *
 * @property handleOnBackPressed callback object that execute some action when back button pressed.
 */
class OnBackPressedCallbackWrapper(
    enabled: Boolean,
    private val handleOnBackPressed: () -> Unit,
) : OnBackPressedCallback(enabled) {

    override fun handleOnBackPressed() {
        handleOnBackPressed.invoke()
    }
}
