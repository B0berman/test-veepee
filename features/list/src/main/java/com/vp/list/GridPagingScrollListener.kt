package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener internal constructor(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {

    var loadMoreItemsListener: LoadMoreItemsListener = EMPTY_LISTENER
    private var isLastPage = false
    private var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadNextPage()) {
            loadMoreItemsListener.loadMoreItems(getNextPageNumber())
        }
    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress() && userScrollsToNextPage() && isNotFirstPage() && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition() >= layoutManager.getItemCount()
    }

    private fun isNotFirstPage(): Boolean {
        return layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.getItemCount() >= PAGE_SIZE
    }

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    private fun isNotLoadingInProgress(): Boolean {
        return !isLoading
    }

    private fun getNextPageNumber(): Int {
        return layoutManager.getItemCount() / PAGE_SIZE + 1
    }

    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    companion object {
        private const val PAGE_SIZE = 10
        private val EMPTY_LISTENER = object : LoadMoreItemsListener {
            override fun loadMoreItems(page: Int) {
            }
        }
    }
}