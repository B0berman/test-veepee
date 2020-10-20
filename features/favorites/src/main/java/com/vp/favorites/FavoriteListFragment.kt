package com.vp.favorites

import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorite.model.Movie
import com.vp.favorites.R.layout
import com.vp.favorites.databinding.FragmentFavoritesBinding
import com.vp.favorites.databinding.ItemMovieBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.math.ceil

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

interface OnFavoriteActionListener {
    fun onItemClicked(id: String)
    fun onRemove(id: String)
}

class FavoriteAdapter(
    private val listener: OnFavoriteActionListener
) : ListAdapter<Movie, FavoriteViewHolder>(
    object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.create(LayoutInflater.from(parent.context), parent, listener)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FavoriteViewHolder(
    private val binding: ItemMovieBinding,
    private val listener: OnFavoriteActionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Movie) {
        binding.apply {
            movieCardTitle.text = item.title
            movieCardDirectorValue.text = item.director
            movieCardRuntimeValue.text = item.runtime
            movieCardPlotValue.text = item.plot
            movieCardReleaseDateValue.text = item.year
            Glide.with(movieCardPoster).load(item.posterUri).into(movieCardPoster)
            movieCardDelete.setOnClickListener { listener.onRemove(item.id) }
            root.setOnClickListener { listener.onItemClicked(item.id) }
        }
    }

    companion object {
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            listener: OnFavoriteActionListener
        ): FavoriteViewHolder {
            val binding = ItemMovieBinding.inflate(inflater, parent, false)
            return FavoriteViewHolder(binding, listener)
        }
    }
}

val Int.dp: Int
    get() = ceil(this * Resources.getSystem().displayMetrics.density).toInt()

class OffsetItemDecoration(
    private val orientation: Int,
    private val offsetPx: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (position == itemCount - 1 || position < 0 || position >= itemCount) return

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (offsetPx > 0) {
                outRect.right = offsetPx
            }
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            if (offsetPx > 0) {
                outRect.bottom = offsetPx
            }
        }
    }
}
