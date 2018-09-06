package com.vp.favorites

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vp.database.model.FavoriteMovie
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var items: List<FavoriteMovie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))
                .apply {
                    itemView.setOnClickListener {
                        listener?.onItemClick(items[adapterPosition].id)
                    }
                }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.title.text = items[position].title
    }

    fun setItems(items: List<FavoriteMovie>) {
        this.items = items
        notifyDataSetChanged()
    }

    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(movieId: String)
    }
}