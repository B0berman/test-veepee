package com.vp.list;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vp.list.search.CustomSearchView;
import com.vp.list.viewmodel.ListViewModel;
import com.vp.list.viewmodel.SearchResult;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ListFragment extends Fragment implements GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {
    private static final String SEARCH_VIEW_STATE = "search_view_state";
    public static final String TAG = "ListFragment";
    @Inject
    ViewModelProvider.Factory factory;

    private CustomSearchView searchView;
    private Bundle searchViewState;
    private ListViewModel listViewModel;
    private GridPagingScrollListener gridPagingScrollListener;
    private ListAdapter listAdapter;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ViewGroup errorContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        listViewModel = new ViewModelProvider(this, factory).get(ListViewModel.class);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        viewAnimator = view.findViewById(R.id.viewAnimator);
        progressBar = view.findViewById(R.id.progressBar);
        errorContainer = view.findViewById(R.id.errorContainer);
        if (savedInstanceState == null) {
            listViewModel.searchMoviesByTitle("Interview");

        } else {
            searchViewState = savedInstanceState.getParcelable(SEARCH_VIEW_STATE);
        }

        initBottomNavigation(view);
        initList();
        listViewModel.observeMovies().observe(getViewLifecycleOwner(), searchResult -> {
            if (searchResult != null) {
                handleResult(listAdapter, searchResult);
            }
        });
        showProgressBar();
        view.findViewById(R.id.retry).setOnClickListener(v -> {
            listViewModel.loadCurrentPageAndTitle();
        });
    }

    private void initBottomNavigation(@NonNull View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.favorites) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"));
                intent.setPackage(requireContext().getPackageName());
                startActivity(intent);
            }
            return true;
        });
    }

    private void initList() {
        listAdapter = new ListAdapter();
        listAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        recyclerView.setLayoutManager(layoutManager);

        // Pagination
        gridPagingScrollListener = new GridPagingScrollListener(layoutManager);
        gridPagingScrollListener.setLoadMoreItemsListener(this);
        recyclerView.addOnScrollListener(gridPagingScrollListener);
    }

    private void showProgressBar() {
        gridPagingScrollListener.markLoading(true);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(progressBar));
    }

    private void showList() {
        gridPagingScrollListener.markLoading(false);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(recyclerView));
    }

    private void showError() {
        gridPagingScrollListener.markLoading(false);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(errorContainer));
    }

    private void handleResult(@NonNull ListAdapter listAdapter, @NonNull SearchResult searchResult) {
        switch (searchResult.getListState()) {
            case LOADED: {
                setItemsData(listAdapter, searchResult);
                showList();
                break;
            }
            case IN_PROGRESS: {
                showProgressBar();
                break;
            }
            default: {
                showError();
            }
        }
    }

    private void setItemsData(@NonNull ListAdapter listAdapter, @NonNull SearchResult searchResult) {
        listAdapter.setItems(searchResult.getItems());
        gridPagingScrollListener.markLastPage(searchResult.getTotalResult() < listAdapter.getItemCount());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            listViewModel.loadFirstPageForCurrentTitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        searchView = (CustomSearchView) menuItem.getActionView();
        if (searchViewState != null) {
            searchView.onRestoreInstanceState(searchViewState);
        }

        searchView.setOnSubmitQueryListener(this::submitSearchQuery);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SEARCH_VIEW_STATE, searchView.onSaveInstanceState());
    }

    @Override
    public void loadMoreItems(int page) {
        listViewModel.loadNextPageForCurrentQuery();
    }

    public void submitSearchQuery(@NonNull final String query) {
        listAdapter.clearItems();
        listViewModel.searchMoviesByTitle(query);
        showProgressBar();
    }

    @Override
    public void onItemClick(String imdbID) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?id=" + imdbID));
        intent.setPackage(requireContext().getPackageName());
        startActivity(intent);
    }
}
