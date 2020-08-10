package com.vp.favorites

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.databinding.FragmentFavouritesListBinding
import com.vp.favorites.model.FavouriteListState
import com.vp.favorites.viewmodel.FavouriteListViewModel
import com.vp.list.ListAdapter
import com.vp.list.model.ListItem
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favourites_list.view.*
import java.lang.IllegalStateException
import javax.inject.Inject

class FavouriteListFragment : Fragment()  {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewDataBinding: ViewDataBinding
    private val viewModel: FavouriteListViewModel by lazy { ViewModelProviders.of(this@FavouriteListFragment, factory).get(FavouriteListViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            FragmentFavouritesListBinding.inflate(inflater, container, false).apply {
                viewDataBinding = this
                vm = viewModel
            }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        viewDataBinding.setLifecycleOwner(viewLifecycleOwner)
        lifecycle.addObserver(viewModel)
    }

    private fun initView(view: View) {
        view.recyclerView.run {
            adapter = ListAdapter()
            layoutManager = GridLayoutManager(context, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        }
    }

    companion object {
        const val TAG = "FavouriteListFragment"
        fun newInstance() = FavouriteListFragment()
    }
}

@BindingAdapter("favouriteListItems")
internal fun setFavouriteListItems(recyclerView: RecyclerView, items: List<ListItem>?) {
    items?:return
    recyclerView.adapter.run {
        if (this !is ListAdapter) {
            throw  IllegalStateException("Do not use favouriteListItems for adapter different than  ListAdapter")
        }
        setItems(items)
    }
}
@BindingAdapter("favouriteListState")
internal fun setFavouriteListState(viewAnimator: ViewAnimator, listState: FavouriteListState?) {
    when (listState) {
        FavouriteListState.FETCHED -> viewAnimator.displayedChild = viewAnimator.indexOfChild(viewAnimator.recyclerView)
        FavouriteListState.FETCHING -> viewAnimator.displayedChild = viewAnimator.indexOfChild(viewAnimator.progressBar)
        else -> viewAnimator.displayedChild = viewAnimator.indexOfChild(viewAnimator.emptyText)
    }
}