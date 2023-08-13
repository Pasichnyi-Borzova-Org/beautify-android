package com.opasichnyi.beautify.util.ext

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph

fun NavController.navigateActionSafe(navDirections: NavDirections) {
    val destinationId = currentDestination?.getAction(navDirections.actionId)?.destinationId
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        destinationId?.let {
            currentNode?.findNode(it)?.let {
                navigate(navDirections)
            }
        }
    }
}