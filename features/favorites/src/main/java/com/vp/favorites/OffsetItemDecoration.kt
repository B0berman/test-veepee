package com.vp.favorites

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import kotlin.math.ceil

class OffsetItemDecoration(
    private val orientation: Int,
    private val offsetPx: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (position == itemCount - 1 || position < 0 || position >= itemCount) return

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (offsetPx > 0) {
                outRect.right = offsetPx
            }
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            if (offsetPx > 0) {
                outRect.bottom = offsetPx
            }
        }
    }
}

val Int.dp: Int
    get() = ceil(this * Resources.getSystem().displayMetrics.density).toInt()
