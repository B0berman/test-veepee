package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.favorites.R.layout
import com.vp.favorites.databinding.FragmentFavoritesBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteListFragment : Fragment(layout.fragment_favorites) {

    @Inject
    lateinit var factory: Factory

    private var binding: FragmentFavoritesBinding? = null
    private fun requireBinding() = binding ?: error("view destroyed")

    private val onFavoriteActionListener = object : OnFavoriteActionListener {
        override fun onItemClicked(id: String) {
            showDetailActivity(id)
        }

        override fun onRemove(id: String) {
            viewModel.remove(id)
        }
    }

    private fun showDetailActivity(id: String) {
        val detailUri = Uri.parse("app://movies/detail")
            .buildUpon()
            .appendQueryParameter("imdbID", id)
            .appendQueryParameter("isFavorite", "true")
            .build()
        val intent = Intent(Intent.ACTION_VIEW, detailUri)
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    private val favoriteAdapter = FavoriteAdapter(onFavoriteActionListener)

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(viewModelStore, factory).get(FavoriteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        setupRecycler()
        bindObservers()
    }

    private fun setupRecycler() {
        val binding = requireBinding()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).also {
                addItemDecoration(OffsetItemDecoration(it.orientation, 8.dp))
            }
            adapter = favoriteAdapter
        }
    }

    private fun bindObservers() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            Log.d(TAG, "movies: ${movies.joinToString { it.title }}")
            favoriteAdapter.submitList(movies)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "FavoriteListFragment"
    }
}

