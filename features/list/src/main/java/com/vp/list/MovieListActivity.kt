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
    private var searchViewIconified = ICONIFIED_DEFAULT
    private var searchQuery = QUERY_DEFAULT

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
            searchViewIconified = savedInstanceState.getBoolean(ICONIFIED_KEY, ICONIFIED_DEFAULT)
            searchQuery = savedInstanceState.getString(QUERY_KEY, QUERY_DEFAULT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        searchView = (menu.findItem(R.id.search).actionView as SearchView).apply {
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            isIconified = searchViewIconified
            setQuery(searchQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    (supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?)
                            ?.submitSearchQuery(query)
                    this@apply.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    searchQuery = newText
                    return false
                }
            })
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchView?.let { outState.putBoolean(ICONIFIED_KEY, it.isIconified) }
        outState.putString(QUERY_KEY, searchQuery)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = dispatchingActivityInjector

    companion object {
        private const val QUERY_KEY = "search_query"
        private const val QUERY_DEFAULT = ""
        private const val ICONIFIED_KEY = "is_search_view_iconified"
        private const val ICONIFIED_DEFAULT = true
    }
}
