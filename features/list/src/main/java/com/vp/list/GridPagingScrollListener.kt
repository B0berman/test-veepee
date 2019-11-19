package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    companion object {
        private val PAGE_SIZE = 10
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    interface LastVisibleItemListener {
        fun onLastItemVisible(position: Int)
    }

    private var loadMoreItemsListener: LoadMoreItemsListener? = null
    private var lastVisibleItemListener: LastVisibleItemListener? = null
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
        lastVisibleItemListener?.onLastItemVisible(layoutManager.childCount + layoutManager.findFirstVisibleItemPosition())
        if (shouldLoadNextPage()) {
            loadMoreItemsListener?.loadMoreItems(nextPageNumber)
        }
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

    fun setLastItemVisibleListener(lastVisibleItemListener: LastVisibleItemListener) {
        this.lastVisibleItemListener = lastVisibleItemListener
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }
}
