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

    var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>? = null
        @Inject set(value) {
            field = value
        }

    private var searchView: SearchView? = null
    private var searchViewExpanded = true
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
            searchViewExpanded = savedInstanceState.getBoolean(Constants.IS_SEARCH_VIEW_ICONIFIED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        menuItem.expandActionView()
        searchView = menuItem.actionView as SearchView
        searchView?.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        searchView?.isIconified = searchViewExpanded
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val listFragment: ListFragment? = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
                listFragment?.submitSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                (supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?)?.updateCurrentQuery(newText)
                return false
            }
        })
        val listFragment: ListFragment? = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?
        if (listFragment != null) {
            searchView?.post {
                menuItem.expandActionView()
                searchView?.setQuery(listFragment.currentQuery, false)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(Constants.IS_SEARCH_VIEW_ICONIFIED, searchView?.isIconified ?: false)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment?>? {
        return dispatchingActivityInjector
    }
}