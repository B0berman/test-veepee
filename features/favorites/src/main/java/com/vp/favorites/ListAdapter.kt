package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.model.FavoriteMovie
import kotlinx.android.synthetic.main.item_favorite.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    internal var movies: List<FavoriteMovie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    internal var onItemClickListener: (FavoriteMovie) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], onItemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FavoriteMovie, onItemClickListener: (FavoriteMovie) -> Unit) {
            itemView.setOnClickListener { onItemClickListener.invoke(item) }

            if (item.poster != null && NO_IMAGE != item.poster) {
                Glide
                        .with(itemView.poster)
                        .load(item.poster)
                        .into(itemView.poster)
            } else {
                itemView.poster.setImageResource(R.drawable.placeholder)
            }
        }
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}