package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import saha.tushar.common.db.FavMovie

class FavMovieAdapter(private val onClick: ((String) -> Unit)? = null) : RecyclerView.Adapter<FavMovieAdapter.ListViewHolder>() {
    private var listItems: List<FavMovie> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, _, _, _, _, _, _, poster) = listItems[position]
        if (NO_IMAGE != poster) {
            val density = holder.image.resources.displayMetrics.density
            Glide.with(holder.image)
                    .load(poster)
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder)
                            .override((300 * density).toInt(), (600 * density).toInt()))
                    .into(holder.image)
            holder.itemView.setOnClickListener {
                onClick?.invoke(listItems[position].imdbId)
            }
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<FavMovie>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.poster)
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}