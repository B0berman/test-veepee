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
    private var searchTypedText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment(), ListFragment.Companion.TAG)
                .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
            searchTypedText = savedInstanceState.getString(SEARCH_TYPED_TEXT, "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu?.findItem(R.id.search)
        searchView = menuItem?.actionView as SearchView
        searchView?.apply {
            setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI)
            setIconified(searchViewExpanded)
            setQuery(searchTypedText, false)
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val listFragment = supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment
                    listFragment.submitSearchQuery(query)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView?.isIconified() ?: false)
        outState?.putString(SEARCH_TYPED_TEXT, searchView?.getQuery().toString())
    }

    override fun onStart() {
        super.onStart()
        clearFocus()
    }

    private fun clearFocus() {
        searchView?.clearFocus()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }

    companion object {
        private val IS_SEARCH_VIEW_ICONIFIED: String? = "is_search_view_iconified"
        private val SEARCH_TYPED_TEXT: String? = "search_typed_text"
    }
}