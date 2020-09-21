package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vp.detail.model.FavoriteMovie

class FavoriteAdapter(private val listItems: List<FavoriteMovie>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    val NO_IMAGE = "N/A"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorit, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val listItem: FavoriteMovie = listItems[position]
        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density: Float = holder.image.resources.displayMetrics.density
            Glide.with(holder.image)
                    .load(listItem.poster)
                    .apply(RequestOptions().override((300 * density).toInt(), (600 * density).toInt()))
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }


    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView


        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View?) {
        }
    }

}
