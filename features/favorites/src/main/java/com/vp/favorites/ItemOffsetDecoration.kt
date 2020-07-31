package com.vp.favorites

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(@DimenRes val offsetId: Int): RecyclerView.ItemDecoration () {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val offset: Int = view.context.resources.getDimensionPixelSize(offsetId)
        val position = parent.getChildAdapterPosition(view)

        val items = parent.adapter?.itemCount ?: 0

        if(parent.layoutManager is GridLayoutManager){
            setGridLayout(parent, items, position, offset, outRect)
        }else if (parent.layoutManager is LinearLayoutManager){
            setLinearLayout(position, offset, outRect)
        }

    }

    private fun setLinearLayout(position: Int, offset: Int, outRect: Rect) {
        val top = if (position == 0) offset else 0
        outRect.set(offset, top, offset, offset)
    }

    private fun setGridLayout(
        parent: RecyclerView,
        items: Int,
        position: Int,
        offset: Int,
        outRect: Rect
    ) {
        val columns = (parent.layoutManager as GridLayoutManager).spanCount
        val rows = (items + 1 / columns)
        val column = getColumn(position, columns)
        val row = getRow(position, columns)

        val topOffset = if (row == 1) offset else offset / 2
        val leftOffset = if (column == 1) offset else offset / 2
        val bottomOffset = if (row == rows) offset else offset / 2
        val rightOffset = if (column == columns) offset else offset / 2

        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset)
    }

    private fun getRow(position: Int, columns: Int) =
        Math.ceil((position.toDouble() + 1) / columns.toDouble()).toInt()

    private fun getColumn(position: Int, columns: Int) = (position % columns) + 1
}