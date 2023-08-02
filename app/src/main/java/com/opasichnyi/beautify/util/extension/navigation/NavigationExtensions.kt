package com.opasichnyi.beautify.util.extension.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.opasichnyi.beautify.presentation.impl.entity.ParcelizeUIEvent
import com.opasichnyi.beautify.presentation.impl.entity.ParcelizeUIEvent.Companion.ParcelizeUIEvent
import com.opasichnyi.beautify.util.extension.common.tryOrNull
import com.opasichnyi.beautify.util.extension.widget.baseActivity
import com.opasichnyi.beautify.util.extension.widget.findNearestParentBaseFragment
import com.opasichnyi.beautify.util.extension.widget.requireBaseActivity
import com.opasichnyi.beautify.util.extension.widget.requireNearestParentBaseFragment
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import kotlin.reflect.KClass

val Fragment.activityNavController: NavController?
    get() = baseActivity?.navController

fun Fragment.requireActivityNavController(): NavController =
    requireBaseActivity().navController

val Fragment.parentNavController: NavController?
    get() = findNearestParentBaseFragment()?.navController

fun Fragment.requireParentNavController(): NavController =
    requireNearestParentBaseFragment().navController

/**
 * Navigate at or above the current level with the use of action.
 *
 * @param directions [NavDirections]
 * @param navOptions [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 *
 * @return true if success navigated
 */
fun Fragment.navigate(
    directions: NavDirections,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController = findNavController(directions.actionId),
) {
    navigate(
        resId = directions.actionId,
        args = directions.arguments,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
        navController = navController,
    )
}

/**
 * Navigate at or above the current level with the use of action.
 *
 * @param resId action
 * @param args [NavController.navigateSafe]
 * @param navOptions [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 *
 * @return true if success navigated
 */
fun Fragment.navigate(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController = findNavController(resId),
) {
    navController.navigate(resId, args, navOptions, navigatorExtras)
}

/**
 * Try to navigate at or above the current level with the use of action.
 *
 * @param directions [NavDirections]
 * @param navOptions [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 *
 * @return true if success navigated
 */
fun Fragment.navigateSafe(
    directions: NavDirections,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController? = tryOrNull { findNavController(directions.actionId) },
): Boolean =
    navigateSafe(
        resId = directions.actionId,
        args = directions.arguments,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
        navController = navController,
    )

/**
 * Try to navigate at or above the current level with the use of action.
 *
 * @param resId action
 * @param args [NavController.navigateSafe]
 * @param navOptions [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 * @param navigatorExtras [NavController.navigateSafe]
 *
 * @return true if success navigated
 */
fun Fragment.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    navController: NavController? = tryOrNull { findNavController(resId) },
): Boolean =
    if (navController == null) {
        Timber.w("Nav controller wasn't found for resource $resId")
        false
    } else {
        navController.navigateSafe(resId, args, navOptions, navigatorExtras)
    }

/**
 * Find a nav controller to execute the action at or above the current nesting level.
 *
 * @param resId action
 */
fun Fragment.findNavController(
    @IdRes resId: Int,
): NavController {
    val navController = findNavController()
    var navDestination: NavDestination? = null
    val onDestinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        navDestination = destination
    }

    // for - val navDestination = navController.backQueue.last().destination
    navController.addOnDestinationChangedListener(onDestinationListener)
    navController.removeOnDestinationChangedListener(onDestinationListener)

    val currentDestination = navDestination ?: navController.graph
    val navigableObject = currentDestination.getAction(resId) ?: navController.graph.findNode(resId)

    return if (navigableObject == null) {
        parentFragment?.findNavController(resId) ?: error("no current navigation node")
    } else {
        navController
    }
}

/**
 * Tries to navigate and in case of an exception does not crash the application,
 * but returns a negative value.
 *
 * @param resId [NavController.navigate]
 * @param args [NavController.navigate]
 * @param navOptions [NavController.navigate]
 * @param navigatorExtras [NavController.navigate]
 *
 * @return true if success navigated
 */
fun NavController?.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) = runCatching {
    this?.navigate(
        resId = resId,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}.onFailure(Timber::e).isSuccess

fun Context.navigate(
    kClass: KClass<out Activity>,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    navigate(
        intent = Intent(this, kClass.java),
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

fun Context.navigate(
    intent: Intent,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    ActivityNavigator(this).navigate(
        intent = intent,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

fun ActivityNavigator.navigate(
    intent: Intent,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    intent.getDestination(this).navigate(
        navigator = this,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

@CheckResult
fun Intent.getDestination(navigator: ActivityNavigator): ActivityNavigator.Destination =
    navigator.createDestination().setIntent(this)

fun ActivityNavigator.Destination.navigate(
    navigator: ActivityNavigator,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
): NavDestination? =
    navigator.navigate(
        destination = this,
        args = args,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )

/**
 * Returns [StateFlow] with navigation result by [key]
 *
 * WARNING: If will you not use [removeNavigationResult],
 * the result will be saved on this to all lifecycle events on the current screen.
 *
 * See Ex:
 * You have a list with items that open a screen with results.
 * After the second, third, ... thousandth results, every result will be saved in for reuse.
 * After [Activity.onSaveInstanceState] or [Fragment.onSaveInstanceState] you can get:
 * "[java.lang.RuntimeException]:
 * [android.os.TransactionTooLargeException]: data parcel size ### bytes"
 */
fun <T : Parcelable> NavController.getNavigationResultStateFlow(
    key: String,
    initialValue: ParcelizeUIEvent<T, Throwable> = ParcelizeUIEvent(),
): StateFlow<ParcelizeUIEvent<T, Throwable>>? =
    currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(key, initialValue)
        ?.takeIf { !it.value.isEmptyState }

/**
 * Saves navigation result by [key]
 */
fun <T : Parcelable> NavController.putNavigationResult(
    key: String,
    result: T,
    resultEvent: ParcelizeUIEvent<T, Throwable> = ParcelizeUIEvent(result),
) {
    previousBackStackEntry?.savedStateHandle?.set(key, resultEvent)
}

/**
 * Remove saves navigation result by [key]
 */
fun NavController.removeNavigationResult(key: String) =
    previousBackStackEntry?.savedStateHandle?.remove<Any>(key) != null
