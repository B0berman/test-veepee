package com.vp.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener internal constructor(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    private var loadMoreItemsListener = EMPTY_LISTENER
    private val scrollDown = MutableLiveData<Boolean>()
    private var isLastPage = false
    private var isLoading = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
//        Log.i("RecyclerView scrolled: ", dy.toString())
        if (shouldLoadNextPage()) {
            loadMoreItemsListener.loadMoreItems(nextPageNumber)
        }
        if (dy <= 0)
            scrollDown.postValue(false)
        else
            scrollDown.postValue(true)

    }

    fun getScrollDown(): LiveData<Boolean> {
        return scrollDown
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

    fun setLoadMoreItemsListener(loadMoreItemsListener: LoadMoreItemsListener?) {
        if (loadMoreItemsListener != null) {
            this.loadMoreItemsListener = loadMoreItemsListener
        } else {
            this.loadMoreItemsListener = EMPTY_LISTENER
        }
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
        private const val PAGE_SIZE = 1
        private val EMPTY_LISTENER: LoadMoreItemsListener = object : LoadMoreItemsListener {
            override fun loadMoreItems(page: Int) {

            }
        }
    }

}