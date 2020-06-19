package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vp.data.model.MovieFavorite
import java.util.*

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private val NO_IMAGE = "N/A"
    private var listItems = Collections.emptyList<MovieFavorite>()

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent,false)
        return FavoritesViewHolder(item, onItemClickListener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = listItems[position]

        holder.txtTitle.text = item.title
        holder.txtYear.text = item.year

        if (NO_IMAGE != item.poster) {
            val density = holder.image.resources.displayMetrics.density
            Glide
                .with(holder.image)
                .load(item.poster)
                .apply(RequestOptions().override((300 * density).toInt(), (600 * density).toInt()))
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }


    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<MovieFavorite>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    class FavoritesViewHolder(itemView: View, private val clickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: ImageView
        val txtTitle: TextView
        val txtYear: TextView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtYear = itemView.findViewById(R.id.txtYear)
        }

        override fun onClick(p0: View?) {
            clickListener?.onItemClick(adapterPosition)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
