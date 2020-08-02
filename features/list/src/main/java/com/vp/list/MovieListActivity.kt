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

private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
private const val SEARCH_QUERY = "search_query"
class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>
    private var searchView: SearchView? = null
    private var searchViewExpanded = true
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        savedInstanceState?.let {
            searchViewExpanded = it.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            searchQuery = it.getString(SEARCH_QUERY)
        } ?: supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        searchView = (menuItem.actionView as? SearchView)?.apply{
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            isIconified = searchViewExpanded
            setQuery(searchQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    (supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment?)?.apply{
                        submitSearchQuery(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView?.isIconified ?: false)
        outState.putString(SEARCH_QUERY, searchView?.query.toString())
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingActivityInjector

}