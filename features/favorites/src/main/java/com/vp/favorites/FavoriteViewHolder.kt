package com.vp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vp.favorite.model.Movie
import com.vp.favorites.databinding.ItemMovieBinding

class FavoriteViewHolder(
    private val binding: ItemMovieBinding,
    private val listener: OnFavoriteActionListener
) : ViewHolder(binding.root) {
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
