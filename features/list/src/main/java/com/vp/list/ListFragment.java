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
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vp.list.viewmodel.ListViewModel;
import com.vp.list.viewmodel.SearchResult;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.AndroidSupportInjection;

public class ListFragment extends Fragment implements GridPagingScrollListener.LoadMoreItemsListener,
        ListAdapter.OnItemClickListener {
    static final String TAG = "ListFragment";
    private static final String CURRENT_QUERY = "current_query";
    private static final String URI_PATH_DETAIL = "detail";
    private static final String URI_PATH_FAVORITES = "favorites";
    private static final String QUERY_PARAM_MOVIE_ID = "?imdbID=";
    private final String URI_BASE = "app://movies/";

    @Inject
    ViewModelProvider.Factory factory;

    private ListViewModel listViewModel;
    private GridPagingScrollListener gridPagingScrollListener;
    private ListAdapter listAdapter;
    private ViewAnimator viewAnimator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private String currentQuery = "Interview";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel.class);
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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progressBar);
        errorTextView = view.findViewById(R.id.errorText);

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY);
        }

        swipeRefreshLayout.setOnRefreshListener(listViewModel);

        initBottomNavigation(view);
        initList();
        listViewModel.observeMovies().observe(this, searchResult -> {
            if (searchResult != null) {
                handleResult(listAdapter, searchResult);
            }
        });
        listViewModel.searchMoviesByTitle(currentQuery, 1);
        showProgressBar();
    }


    // region Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list_fragment_options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            listViewModel.onRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // endregion Menu

    private void initBottomNavigation(@NonNull View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.favorites) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI_BASE+ URI_PATH_FAVORITES));
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
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(progressBar));
    }

    private void showList() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(swipeRefreshLayout));
    }

    private void showError() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(errorTextView));
    }

    private void handleResult(@NonNull ListAdapter listAdapter, @NonNull SearchResult searchResult) {
        switch (searchResult.getListState()) {
            case LOADED: {
                setItemsData(listAdapter, searchResult);
                swipeRefreshLayout.setRefreshing(false);
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
        gridPagingScrollListener.markLoading(false);
    }

    private void setItemsData(@NonNull ListAdapter listAdapter, @NonNull SearchResult searchResult) {
        listAdapter.setItems(searchResult.getItems());

        if (searchResult.getTotalResult() <= listAdapter.getItemCount()) {
            gridPagingScrollListener.markLastPage(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_QUERY, currentQuery);
    }

    @Override
    public void loadMoreItems(int page) {
        gridPagingScrollListener.markLoading(true);
        listViewModel.searchMoviesByTitle(currentQuery, page);
    }

    void submitSearchQuery(@NonNull final String query) {
        currentQuery = query;
        listAdapter.clearItems();
        listViewModel.searchMoviesByTitle(query, 1);
        showProgressBar();
    }

    @Override
    public void onItemClick(String imdbID) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(URI_BASE+ URI_PATH_DETAIL + QUERY_PARAM_MOVIE_ID +imdbID));
        intent.setPackage(requireContext().getPackageName());
        startActivity(intent);
    }

}
