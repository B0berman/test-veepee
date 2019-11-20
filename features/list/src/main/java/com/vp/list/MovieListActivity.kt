package com.vp.list

import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        private val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private val SEARCH_VIEW_VALUE = "search_view_value"
    }

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>
    private var searchView: SearchView? = null
    private var searchViewExpanded = true
    private var searchValue: String? = ""

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
            searchValue = savedInstanceState.getString(SEARCH_VIEW_VALUE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)

        searchView = menuItem.actionView as SearchView
        searchView?.let {
            it.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            it.isIconified = searchViewExpanded
            it.setQuery(searchValue, false)
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
                    listFragment?.submitSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }


        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchView?.let {
            outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, it.isIconified)
            outState.putString(SEARCH_VIEW_VALUE, it.query.toString())
        }

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return dispatchingActivityInjector
    }
}
