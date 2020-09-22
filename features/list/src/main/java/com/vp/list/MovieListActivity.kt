package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vp.list.R.id
import com.vp.list.R.layout
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasSupportFragmentInjector {
  @Inject
  lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>
  private lateinit var searchView: SearchView
  private var searchViewExpanded = true
  private var searchViewQuery = ""

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
      searchViewQuery = savedInstanceState.getString(SEARCH_VIEW_QUERY)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.options_menu, menu)
    val menuItem = menu.findItem(id.search)
    configureSearchView(menuItem)
    return true
  }

  private fun configureSearchView(menuItem: MenuItem) {
    searchView = menuItem.actionView as SearchView
    searchView.apply {
      imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
      isIconified = searchViewExpanded
      setQuery(searchViewQuery, true)
      setOnQueryTextListener(object : OnQueryTextListener {
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
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
    outState.putString(SEARCH_VIEW_QUERY, searchView.query.toString())
  }

  override fun supportFragmentInjector() = dispatchingActivityInjector

  companion object {
    private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
    private const val SEARCH_VIEW_QUERY = "search_view_query"
  }
}