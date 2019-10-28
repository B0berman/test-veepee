package com.vp.favorites.views

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.R
import com.vp.favorites.utils.getPxDimension


class VerticalItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var startOffset = context.getPxDimension(R.dimen.margin_vertical_container)
        var endOffset = context.getPxDimension(R.dimen.margin_vertical_container)

        if (isFirstItem(view, parent)) {
            startOffset = startOffset.times(DOUBLE)
        }

        if (isLastItem(view, parent)) {
            endOffset = endOffset.times(DOUBLE)
        }

        with(outRect) {
            top = startOffset
            bottom = endOffset
        }
    }

    private fun isFirstItem(view: View, parent: RecyclerView) : Boolean {
        return parent.getChildAdapterPosition(view) == FIRST
    }

    private fun isLastItem(view: View, parent: RecyclerView) : Boolean {
        val lastItemIndex = parent.adapter?.itemCount?.dec() ?: INVALID
        return parent.getChildAdapterPosition(view) == lastItemIndex
    }

    companion object {
        private const val INVALID = -1
        private const val FIRST = 0
        private const val DOUBLE = 2
    }
}
