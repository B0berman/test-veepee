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
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    private var searchView: SearchView? = null
    private var searchViewExpanded = true
    private var queryingText = ""

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
            queryingText = savedInstanceState.getString(QUERYING_TEXT) ?: ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val refreshItem = menu.findItem(R.id.refresh)

        searchView = searchItem.actionView as SearchView
        searchView?.let { view ->
            view.setQuery(queryingText, false)
            view.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            view.isIconified = searchViewExpanded

            view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment
                    listFragment.submitSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String) = false
            })

            refreshItem.setOnMenuItemClickListener {
                val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment
                listFragment.reloadHomePage()
                view.onActionViewCollapsed()
                return@setOnMenuItemClickListener true
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchView?.let {
            outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, it.isIconified)
            outState.putString(QUERYING_TEXT, it.query.toString())
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingActivityInjector

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
        private const val QUERYING_TEXT = "querying_text"
    }
}
