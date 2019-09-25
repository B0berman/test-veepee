package com.vp.favorites;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vp.list.GridPagingScrollListener;
import com.vp.list.model.ListItem;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class ListFragment extends Fragment implements GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {
    public static final String TAG = "ListFragment";
    private static final String CURRENT_QUERY = "current_query";

    @Inject
    ViewModelProvider.Factory factory;

    private List<ListItem> aggregatedItems = new ArrayList<>();
    private GridPagingScrollListener gridPagingScrollListener;
    private ListAdapter listAdapter;
    private ViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private String currentQuery = "Interview";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("ListFragment onCreate");
        super.onCreate(savedInstanceState);
//        AndroidSupportInjection.inject(this);
    // Now in your TargetActivity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            ArrayList<ListItem> litems = (ArrayList<ListItem>)extras.getSerializable(getString(R.string.favorite));
            aggregatedItems.addAll(litems);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        viewAnimator = view.findViewById(R.id.viewAnimator);

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY);
        }

        initList();
        /*
        listViewModel.observeMovies().observe(this, searchResult -> {
            System.out.println("ListFragment onViewCreated listViewModel.observeMovies()");
            if (searchResult != null) {
                handleResult(listAdapter, searchResult);
            }
        });
        listViewModel.searchMoviesByTitle(currentQuery, 1);*/
        showProgressBar();
    }




    private void initList() {
        listAdapter = new ListAdapter();
        listAdapter.setOnItemClickListener(this);
        //listViewModel.setAdapter(listAdapter);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
        recyclerView.setLayoutManager(layoutManager);

        // Pagination
        //gridPagingScrollListener = new GridPagingScrollListener(layoutManager);
        //gridPagingScrollListener.setLoadMoreItemsListener(this);
        //recyclerView.addOnScrollListener(gridPagingScrollListener);

        listAdapter.setItems(aggregatedItems);

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




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_QUERY, currentQuery);
    }



    @Override
    public void onItemClick(String imdbID) {
        //TODO handle click events
        System.out.println("ListFragment onItemClick "+ imdbID);
        try {
        Intent intent = null;

            intent = new Intent(getActivity(), Class.forName("com.vp.detail.DetailActivity"));
            intent.setData(Uri.parse("app://open?imdbID="+imdbID));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreItems(int page) {

    }
}
