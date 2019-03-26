package com.vp.favorites;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vp.favorites.viewmodel.FavouriteListViewModel;

import java.util.List;

import javax.inject.Inject;

import co.uk.missionlabs.db.Model.ListItem;
import dagger.android.support.AndroidSupportInjection;

public class FavouriteListFragment extends Fragment implements GridPagingScrollListener.LoadMoreItemsListener, FavouriteListAdapter.OnItemClickListener {
    public static final String TAG = "ListFragment";

    @Inject
    ViewModelProvider.Factory factory;

    private FavouriteListViewModel favouriteListViewModel;
    private GridPagingScrollListener gridPagingScrollListener;
    private FavouriteListAdapter favouriteListAdapter;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
        favouriteListViewModel = ViewModelProviders.of(this, factory).get(FavouriteListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        viewAnimator = view.findViewById(R.id.viewAnimator);
        progressBar = view.findViewById(R.id.progressBar);
        errorTextView = view.findViewById(R.id.errorText);
        refreshLayout = view.findViewById(R.id.swiperefresh);
        initList();
        favouriteListViewModel.observeFavourites().observe(this, searchResult -> {
            if (searchResult != null) {
                handleResult(favouriteListAdapter, searchResult);
            }
        });

        favouriteListViewModel.getFavourites();

    }

    private void initList() {
        favouriteListAdapter = new FavouriteListAdapter();
        favouriteListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(favouriteListAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        recyclerView.setLayoutManager(layoutManager);

        // Pagination
        gridPagingScrollListener = new GridPagingScrollListener(layoutManager);
        gridPagingScrollListener.setLoadMoreItemsListener(this);
        recyclerView.addOnScrollListener(gridPagingScrollListener);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favouriteListViewModel.getFavourites();
            }
        });

    }

    private void showProgressBar() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(progressBar));
    }

    private void showList() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(recyclerView));
    }

    private void showError() {
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(errorTextView));
    }

    private void handleResult(@NonNull FavouriteListAdapter favouriteListAdapter, @NonNull List<ListItem> searchResult) {
        gridPagingScrollListener.markLoading(false);
        refreshLayout.setRefreshing(false);
        if(searchResult.size()==0){
            showError();
        }else{
            setItemsData(favouriteListAdapter,searchResult);
            showList();
        }
    }

    private void setItemsData(@NonNull FavouriteListAdapter favouriteListAdapter, @NonNull List<ListItem> results) {
        favouriteListAdapter.setItems(results);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void loadMoreItems(int page) {
        gridPagingScrollListener.markLoading(true);
        favouriteListViewModel.getFavourites();
    }

    @Override
    public void onItemClick(String imdbID) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail"));
        intent.putExtra("imdbID",imdbID);
        startActivityForResult(intent,1);

    }
 

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        favouriteListViewModel.getFavourites();
    }
}
