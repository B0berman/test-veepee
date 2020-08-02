package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val PAGE_SIZE = 10
class GridPagingScrollListener internal constructor(
        private val layoutManager: GridLayoutManager,
        private inline val loadMoreItems: ((Int) -> Unit)?= null
) : RecyclerView.OnScrollListener() {

    private var isLastPage = false
    private var isLoading = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (shouldLoadNextPage()) { loadMoreItems?.invoke(nextPageNumber) }
    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
    }

    private val isNotFirstPage: Boolean
        private get() = layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    private val isNotLoadingInProgress: Boolean
        private get() = !isLoading

    private val nextPageNumber: Int
        private get() = layoutManager.itemCount / PAGE_SIZE + 1


    fun markLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    fun markLastPage(isLastPage: Boolean) {
        this.isLastPage = isLastPage
    }

}