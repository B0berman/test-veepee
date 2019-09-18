package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.daggeraddons.GlideApp
import com.vp.persistance.model.FavoriteMovie

/**
 * Created by Uxio Lorenzo on 2019-09-10.
 */
class ListAdapter(
    val onItemClicked: (String) -> Unit
): RecyclerView.Adapter<ListAdapter.ListVH>() {

    var listItems: List<FavoriteMovie> = emptyList()

    fun onItems(items: List<FavoriteMovie>) {
        listItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListVH
        = ListVH(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.favorite_item,
                    parent,
                    false))


    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: ListVH, position: Int) {
        val item = listItems[position]

        if (NO_IMAGE != item.poster) {
            val density = holder.ivPoster.resources.displayMetrics.density
            GlideApp
                .with(holder.ivPoster)
                .load(item.poster)
                .override((300 * density).toInt(), (600 * density).toInt())
                .into(holder.ivPoster)
        } else {
            holder.ivPoster.setImageResource(R.drawable.placeholder)
        }
    }


    inner class ListVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.favorite_poster)

        init {
            itemView.setOnClickListener {
                onItemClicked(listItems[adapterPosition].omdbId)
            }
        }
    }

    companion object {
        const val NO_IMAGE = "N/A"
    }
}