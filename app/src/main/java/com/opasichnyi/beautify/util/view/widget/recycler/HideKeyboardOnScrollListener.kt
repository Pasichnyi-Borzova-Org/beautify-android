package com.opasichnyi.beautify.util.view.widget.recycler

import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.opasichnyi.beautify.util.extension.widget.hideKeyboard

class HideKeyboardOnScrollListener(private val window: Window) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
            recyclerView.hideKeyboard(window)
        }
    }
}
