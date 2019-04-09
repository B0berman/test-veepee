package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    private var searchView: SearchView? = null
    private var searchViewExpanded = true
    private var searchViewQuery: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                    .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            searchViewQuery = savedInstanceState.getCharSequence(SEARCH_VIEW_QUERY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)

        searchView = menuItem.actionView as SearchView
        searchView?.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView?.isIconified = searchViewExpanded
        searchView?.setQuery(searchViewQuery, true) // Query can be is null, no need check nullable before
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
                listFragment!!.submitSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchView != null) {
            outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView!!.isIconified)
            outState.putCharSequence(SEARCH_VIEW_QUERY, searchView!!.query)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val SEARCH_VIEW_QUERY = "search_view_query"
    }
}