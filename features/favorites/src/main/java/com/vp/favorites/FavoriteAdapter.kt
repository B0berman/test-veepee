package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.favorites.model.FavouriteItem

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    var items: List<FavouriteItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
            ListViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_favorite, parent, false)
            ).also {
                it.onItemClickListener = onItemClickListener
            }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView
    ) {

        var onItemClickListener: OnItemClickListener? = null

        private var image: ImageView = itemView.findViewById(R.id.poster)

        fun bind(item: FavouriteItem) {

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item.imdbID)
            }
            if (item.poster != null && NO_IMAGE != item.poster) {
                Glide.with(image).load(item.poster).into(image)
            } else {
                image.setImageResource(R.drawable.placeholder)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }
}

private const val NO_IMAGE = "N/A"
