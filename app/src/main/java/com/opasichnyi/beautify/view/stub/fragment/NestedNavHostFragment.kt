package com.opasichnyi.beautify.view.stub.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class NestedNavHostFragment : NavHostFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = requireArguments()
        val navController = navController
        val navGraphRes = arguments.graphRes ?: throw NullPointerException("Graph is null")
        val graph = navController.navInflater.inflate(navGraphRes)
        arguments.startDestinationId?.let(graph::setStartDestination)
        navController.setGraph(graph, arguments.startDestinationArgs)
    }

    companion object {

        private const val ARG_GRAPH_RES = "ARG_GRAPH_RES"
        private const val ARG_START_DESTINATION_ID = "ARG_START_DESTINATION_ID"
        private const val ARG_START_DESTINATION_ARGS = "ARG_START_DESTINATION_ARGS"

        @get:NavigationRes
        private var Bundle.graphRes: Int?
            get() = getInt(ARG_GRAPH_RES).takeIf { it != ResourcesCompat.ID_NULL }
            set(@NavigationRes value) = putInt(ARG_GRAPH_RES, value ?: ResourcesCompat.ID_NULL)

        @get:IdRes
        private var Bundle.startDestinationId: Int?
            get() = getInt(ARG_START_DESTINATION_ID).takeIf { it != ResourcesCompat.ID_NULL }
            set(@IdRes value) {
                putInt(ARG_START_DESTINATION_ID, value ?: ResourcesCompat.ID_NULL)
            }

        private var Bundle.startDestinationArgs: Bundle?
            get() = getBundle(ARG_START_DESTINATION_ARGS)
            set(value) = putBundle(ARG_START_DESTINATION_ARGS, value)

        fun create(
            @NavigationRes graphResRes: Int,
            @IdRes startDestinationId: Int? = null,
            startDestinationArgs: Bundle? = null,
        ): Fragment = NestedNavHostFragment().apply {
            arguments = Bundle().also { args ->
                args.graphRes = graphResRes
                args.startDestinationId = startDestinationId
                args.startDestinationArgs = startDestinationArgs
            }
        }
    }
}
