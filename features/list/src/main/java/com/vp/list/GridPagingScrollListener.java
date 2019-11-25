package com.vp.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridPagingScrollListener extends RecyclerView.OnScrollListener {
    private static final int PAGE_SIZE = 10;
    private final GridLayoutManager layoutManager;
    /* Should not be static as it's will keep a strong reference to the object(listener), and it's better like this(following The Android Profiler) - CE */
    private LoadMoreItemsListener loadMoreItemsListener;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    GridPagingScrollListener(@NonNull final GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (shouldLoadNextPage()) {
            if(null != loadMoreItemsListener) {
                loadMoreItemsListener.loadMoreItems(getNextPageNumber());
            }
            else {
                // Empty listener
            }
        }
    }

    private boolean shouldLoadNextPage() {
        return isNotLoadingInProgress() && userScrollsToNextPage() && isNotFirstPage() && hasNextPage();
    }

    private boolean userScrollsToNextPage() {
        return (layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount();
    }

    private boolean isNotFirstPage() {
        return layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.getItemCount() >= PAGE_SIZE;
    }

    private boolean hasNextPage() {
        return !isLastPage;
    }

    private boolean isNotLoadingInProgress() {
        return !isLoading;
    }

    private int getNextPageNumber() {
        return layoutManager.getItemCount() / PAGE_SIZE + 1;
    }

    public void setLoadMoreItemsListener(@Nullable final LoadMoreItemsListener loadMoreItemsListener) {
            this.loadMoreItemsListener = loadMoreItemsListener;
    }

    public void markLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void markLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public interface LoadMoreItemsListener {
        void loadMoreItems(int page);
    }
}
