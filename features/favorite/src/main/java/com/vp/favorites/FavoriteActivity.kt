package com.vp.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.vp.favorites.viewmodel.Action
import com.vp.favorites.viewmodel.FavoriteViewModel
import com.vp.favorites.viewmodel.ViewState.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorite.*
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProvider(this, factory).get(FavoriteViewModel::class.java) }

    private val adapter by lazy { DetailAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

        }.attach()
        observeViewModel()
        tryAgainButton.setOnClickListener {
            viewModel.onAction(Action.TryAgain)
        }
        if (savedInstanceState == null) {
            viewModel.onAction(Action.Load)
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer { viewState ->
            loading.visible = viewState is Loading
            emptyStateMessage.visible = viewState is Empty
            tryAgainButton.visible = viewState is Failure

            when (viewState) {
                is Content -> {
                    adapter.setItems(viewState.data)
                }
            }
        })
    }
}

var View.visible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value) {
        visibility = if (value) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
