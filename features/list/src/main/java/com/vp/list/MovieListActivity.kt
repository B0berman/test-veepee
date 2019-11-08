package com.vp.list

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var searchView: SearchView
    private var searchQuery: String? = null
    private var searchViewExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_activity_list)

        //todo recuperar fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                    .commit()
        } else {
            searchQuery = savedInstanceState.getString(SEARCH_QUERY)
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)

        searchView = (menuItem.actionView as SearchView).also {
            it.setQuery(searchQuery, false)
            it.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            it.isIconified = searchViewExpanded
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val listFragment = supportFragmentManager
                            .findFragmentByTag(ListFragment.TAG) as ListFragment?
                    listFragment?.submitSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String) = false
            })
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
        outState.putString(SEARCH_QUERY, searchView.query.toString())
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingActivityInjector
    }

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val SEARCH_QUERY = "search_query"
    }
}
