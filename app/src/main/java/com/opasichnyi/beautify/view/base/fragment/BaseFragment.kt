package com.opasichnyi.beautify.view.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.opasichnyi.beautify.R
import com.opasichnyi.beautify.presentation.base.viewmodel.BaseViewModel
import com.opasichnyi.beautify.util.extension.common.unsafeLazy
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycle
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycleDestroy
import com.opasichnyi.beautify.util.extension.lifecycle.launchOnLifecycleResume
import com.opasichnyi.beautify.util.extension.reflect.getProperty
import com.opasichnyi.beautify.util.extension.widget.createViewModel
import com.opasichnyi.beautify.util.extension.widget.findNearestParentBaseFragment
import com.opasichnyi.beautify.util.extension.widget.inflateBinding
import com.opasichnyi.beautify.util.extension.widget.inflateMenu
import com.opasichnyi.beautify.util.extension.widget.rootBaseFragment
import com.opasichnyi.beautify.util.view.lifecycle.lifecycle
import com.opasichnyi.beautify.util.view.navigation.OnBackPressedCallbackWrapper
import com.opasichnyi.beautify.view.base.BaseView
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity.Menu
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity.NavIcon
import com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity.Title
import timber.log.Timber
import kotlin.properties.Delegates

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> :
    InjectableFragment(),
    BaseView<VB, VM> {

    init {
        arguments = Bundle()
    }

    protected val viewModel: VM by unsafeLazy(::createViewModel)

    protected var binding: VB? = null
        private set

    @StyleRes protected open val themeRes: Int = R.style.Theme_Application_Animation

    protected open val isNavigationIconEnabled: Boolean
        get() = parentFragmentManager.backStackEntryCount > 0 ||
            findNearestParentBaseFragment()?.isNavigationIconEnabled == true

    protected val toolbar: Toolbar? by Delegates.lifecycle(::getViewLifecycleOwner) {
        binding?.root?.findViewById(R.id.toolbar)
    }

    /**
     * AppBarLayout in layout determines if it is root or child. If present, the fragment is root,
     * otherwise child.
     */
    protected val appBarLayout: AppBarLayout? by Delegates.lifecycle(::getViewLifecycleOwner) {
        binding?.root?.findViewById(R.id.appBarLayout)
    }

    protected val isRoot: Boolean by unsafeLazy { appBarLayout != null }

    val navController: NavController by unsafeLazy { findNavController() }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater =
        super.onGetLayoutInflater(savedInstanceState)
            .cloneInContext(ContextThemeWrapper(requireContext(), themeRes))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .also { viewLifecycleOwner.launchOnLifecycleDestroy { binding = null } }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireBinding().let { binding ->
            initBackPressListener()
            initToolbar()
            onViewBound(binding, savedInstanceState)
            listenViewModel(viewModel)
            listenRootData(binding)
        }
    }

    private fun initBackPressListener() {
        val onBackPressedCallback = OnBackPressedCallbackWrapper(false, ::onBackPressed)

        viewLifecycleOwner.launchOnLifecycle { event ->
            onBackPressedCallback.isEnabled = event == Lifecycle.Event.ON_RESUME
        }

        onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    protected open fun onBackPressed() {
        if (navController.navigateUp()) {
            Timber.v("Back navigation completed")
        } else {
            findNearestParentBaseFragment()?.onBackPressed()
                ?: finishActivity()
        }
    }

    protected open fun finishActivity() {
        // The default onBackPressed logic allows app to re-hot launch with softer/faster Splash
        // call OnBackPressedDispatcher.fallbackOnBackPressed.run() -> Activity.onBackPressed()
        OnBackPressedDispatcher::class
            .getProperty<Runnable>(true)
            .get(onBackPressedDispatcher)
            ?.run()
            ?: requireActivity().finishAfterTransition()
    }

    private fun initToolbar() {
        // Init menu and home icons/buttons logic
        toolbar?.let { toolbar ->
            toolbar.setOnMenuItemClickListener(::onMenuItemClick)
            toolbar.setNavigationOnClickListener { onNavigationIconClick() }
        }

        // Set a title when a screen is first initialized
        if (viewModel.getTitle() == null) {
            requireArguments().apply {
                if (containsKey(CURRENT_DESTINATION_LABEL_KEY)) return@apply

                val nullableNewLabel = navController.currentDestination?.label

                nullableNewLabel?.let(viewModel::emitTitle)
                putCharSequence(CURRENT_DESTINATION_LABEL_KEY, nullableNewLabel)
            }
        }

        // Init RootData logic
        viewLifecycleOwner.launchOnLifecycleResume { viewModel.invalidateRootData() }
    }

    private fun onMenuItemClick(menuItem: MenuItem): Boolean {
        if (isHidden || !isResumed) return false
        if (onToolbarItemSelected(menuItem)) return true

        return childFragmentManager
            .fragments
            .asSequence()
            .map { childFragment ->
                if (childFragment is BaseFragment<*, *>) {
                    listOf(childFragment)
                } else {
                    childFragment.childFragmentManager.fragments
                }
            }
            .flatten()
            .mapNotNull { it as? BaseFragment<*, *>? }
            .any { it.onMenuItemClick(menuItem) }
    }

    @Deprecated(
        message = "@see [Fragment.onOptionsItemSelected]",
        replaceWith = ReplaceWith("onToolbarItemSelected(menuItem)"),
        level = DeprecationLevel.ERROR,
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    /**
     * Actual version [onOptionsItemSelected]
     */
    protected open fun onToolbarItemSelected(menuItem: MenuItem) = false

    protected open fun onNavigationIconClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    fun invalidateOptionsMenu() {
        if (isRoot) toolbar?.invalidateMenu() else rootBaseFragment?.toolbar?.invalidateMenu()
    }

    @CallSuper override fun listenViewModel(viewModel: VM) {
        super.listenViewModel(viewModel)
        viewModel
            .navigationIconEnableEvent
            .launchCollectWithView(oneTimeEvent = true) { _, _ ->
                viewModel.emitNavIcon(isNavigationIconEnabled)
            }
    }

    private fun listenRootData(binding: VB) {
        viewModel.launchCollect(viewLifecycleOwner) { rootData ->
            when (rootData) {
                is Title -> onTitleDataChanged(binding, rootData)
                is Menu -> onMenuDataChanged(binding, rootData)
                is NavIcon -> onNavIconDataChanged(binding, rootData)
            }
        }
    }

    /**
     * WARNING! Do not call this function, just override it.
     */
    protected open fun onTitleDataChanged(binding: VB, title: Title) {
        toolbar?.title = title.title
    }

    /**
     * WARNING! Do not call this function, just override it.
     */
    protected open fun onMenuDataChanged(binding: VB, menu: Menu) {
        toolbar?.inflateMenu(menu.menuId, menu.isClearBeforeInflate)
    }

    /**
     * WARNING! Do not call this function, just override it.
     */
    protected open fun onNavIconDataChanged(binding: VB, navIcon: NavIcon) {
        toolbar?.navigationIcon = navIcon.getNavigationIcon(binding.root.context)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args ?: Bundle())
    }

    @CheckResult override fun requireBinding(): VB =
        binding ?: error("Binding in fragment $this is null")

    companion object {

        const val CURRENT_DESTINATION_LABEL_KEY = "CURRENT_DESTINATION_LABEL_KEY"
    }
}
