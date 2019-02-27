package com.vp.list;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MovieListActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String IS_SEARCH_VIEW_ICONIFIED_KEY = "is_search_view_iconified";
    private static final String SEARCH_QUERY_KEY = "search_query_key";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingActivityInjector;
    private SearchView searchView;
    private boolean searchViewExpanded = true;
    private String searchQuery = "Interview"; // not good, provide some default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (savedInstanceState == null) {
            ListFragment rootFragment = new ListFragment();
            Bundle fragArgs = new Bundle();
            fragArgs.putString(ListFragment.INITIAL_QUERY_KEY, searchQuery);
            rootFragment.setArguments(fragArgs);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, rootFragment, ListFragment.TAG)
                    .commit();

        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED_KEY);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, searchQuery);
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(ListFragment.TAG);
                listFragment.submitSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search);
        ((SearchView) menuItem.getActionView()).setQuery(searchQuery, false); // TODO
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_QUERY_KEY, searchQuery);
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED_KEY, searchView.isIconified());
        super.onSaveInstanceState(outState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingActivityInjector;
    }
}
