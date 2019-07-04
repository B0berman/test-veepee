package com.vp.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener internal constructor(
        private val layoutManager: GridLayoutManager,
        private val loadMoreItems: (page: Int) -> Unit
) : RecyclerView.OnScrollListener() {

    var isLastPage = false
    var isLoading = false

    private val isNotFirstPage: Boolean
        get() = layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    private val isNotLoadingInProgress: Boolean
        get() = !isLoading

    private val nextPageNumber: Int
        get() = layoutManager.itemCount / PAGE_SIZE + 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (shouldLoadNextPage()) loadMoreItems(nextPageNumber)
    }

    private fun shouldLoadNextPage(): Boolean {
        return isNotLoadingInProgress && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
    }

    private fun hasNextPage(): Boolean = !isLastPage

    companion object {
        private const val PAGE_SIZE = 10
    }
}
