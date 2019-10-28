package com.vp.list;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.vp.detail.DetailActivity;
import com.vp.list.navigation.ContentNavigation;

import dagger.android.support.DaggerAppCompatActivity;

public class MovieListActivity extends DaggerAppCompatActivity implements ContentNavigation {

    private static final String IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified";
    private static final String SEARCH_QUERY = "search_query";

    private SearchView searchView;
    private boolean searchViewExpanded = true;
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ListFragment(), ListFragment.TAG)
                    .commit();
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setIconified(searchViewExpanded);
        searchView.setQuery(searchQuery, false);
        searchView.setOnCloseListener(() -> {
            Fragment fragment = getFragmentByTag(ListFragment.TAG);
            if (fragment instanceof SearchHandler) {
                ((SearchHandler) fragment).resetSearchQuery();
            }
            return true;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = getFragmentByTag(ListFragment.TAG);
                if (fragment instanceof SearchHandler) {
                    ((SearchHandler) fragment).submitSearchQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY, searchView.getQuery().toString());
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified());
    }

    @Override
    public void navigateToMovieDetail(String movieID) {
        startActivity(DetailActivity.Companion.generateIntent(this, movieID));
    }

    private Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
