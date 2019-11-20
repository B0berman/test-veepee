package com.vp.list

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener(), RecyclerView.OnChildAttachStateChangeListener {

    companion object {
        private val PAGE_SIZE = 10
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    interface VisibleItemListener {
        fun onLastItemVisible(position: Int)

        fun onChildVisibleChange(visibleItems: Int)
    }

    private var loadMoreItemsListener: LoadMoreItemsListener? = null
    private var visibleItemListener: VisibleItemListener? = null
    private var isLastPage = false
    private var isLoading = false

    private val isNotFirstPage: Boolean
        get() = layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    private val isNotLoadingInProgress: Boolean
        get() = !isLoading

    private val nextPageNumber: Int
        get() = layoutManager.itemCount / PAGE_SIZE + 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadNextPage()) {
            loadMoreItemsListener?.loadMoreItems(nextPageNumber)
        }
        visibleItemListener?.onLastItemVisible(layoutManager.childCount + layoutManager.findFirstVisibleItemPosition())
    }

    override fun onChildViewAttachedToWindow(view: View) {
        visibleItemListener?.onChildVisibleChange(layoutManager.childCount)
    }

    override fun onChildViewDetachedFromWindow(view: View) {

    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
    }

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    fun setLoadMoreItemsListener(loadMoreItemsListener: LoadMoreItemsListener?) {
        this.loadMoreItemsListener = loadMoreItemsListener
    }

    fun setLastItemVisibleListener(lastVisibleItemListener: VisibleItemListener) {
        this.visibleItemListener = lastVisibleItemListener
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }
}
