package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import com.vp.list.R.id
import com.vp.list.R.layout
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>

    private lateinit var searchView: SearchView

    private var searchViewExpanded = true
    private var savedQuery: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_movie_list)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(id.fragmentContainer, ListFragment(), ListFragment.TAG)
                .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            savedQuery = savedInstanceState.getCharSequence(CURRENT_QUERY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val refreshItem = menu.findItem(id.refresh)
        refreshItem.setOnMenuItemClickListener { item: MenuItem? ->
            val listFragment = supportFragmentManager.findFragmentByTag(
                ListFragment.TAG
            ) as ListFragment?
            listFragment!!.refresh()
            true
        }
        val menuItem = menu.findItem(id.search)
        searchView = menuItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView.isIconified = searchViewExpanded
        searchView.setQuery(savedQuery, false)
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val listFragment =
                    supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
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
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
        outState.putCharSequence(CURRENT_QUERY, searchView.query.toString())
    }

    override fun androidInjector() = dispatchingActivityInjector

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val CURRENT_QUERY = "current_query"
    }
}
