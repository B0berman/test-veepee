package com.vp.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.favorites.model.BasicMovie
import com.vp.favorites.viewmodel.FavoriteViewModel
import com.vp.favorites.views.VerticalItemDecorator
import com.vp.favorites.views.adapter.BasicMovieAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: FavoriteViewModel

    private var moviesAdapter: BasicMovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        initViews()
        viewModel.getMyFavoriteMovies()
        subscribeToUiEvents()
    }

    private fun initViews() {
        setupList()
        showProgressBar()
    }

    private fun subscribeToUiEvents() {
        viewModel.movies().observe(this, Observer {
            showList()
            setAdapter(it)
        })

        viewModel.error().observe(this, Observer { showError() })
    }

    private fun setupList() {
        setAdapter()
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalItemDecorator(context))
            this.adapter = moviesAdapter
        }
    }

    private fun setAdapter(list: List<BasicMovie> = emptyList()) {
        moviesAdapter?.run {
            updateItems(list)
        } ?: run{
            moviesAdapter = BasicMovieAdapter(this, list)
        }
    }

    private fun showList() { displayChild(recyclerView) }
    private fun showProgressBar() { displayChild(progressBar) }
    private fun showError() { displayChild(errorText) }

    private fun displayChild(child: View) {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(child)
    }
}