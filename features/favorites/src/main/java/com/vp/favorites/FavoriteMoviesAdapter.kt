package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.model.FavoriteMovie

/**
 * Created by Albert Vila Calvo on 2020-02-07.
 */
class FavoriteMoviesAdapter : ListAdapter<FavoriteMovie, FavoriteMoviesAdapter.ViewHolder>(
        ItemCallback()
) {

    private class ItemCallback : DiffUtil.ItemCallback<FavoriteMovie>() {
        override fun areItemsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.item_favorite_movie_title_textView)
        private val yearTextView: TextView = itemView.findViewById(R.id.item_favorite_movie_year_textView)

        internal fun bind(favoriteMovie: FavoriteMovie) {
            titleTextView.text = favoriteMovie.title
            yearTextView.text = favoriteMovie.year
        }
    }

}
