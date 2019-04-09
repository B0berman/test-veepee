package com.vp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.databases.model.FavoriteMovie
import java.util.Collections.emptyList

class ListAdapter(private var listItems: MutableList<FavoriteMovie> = emptyList<FavoriteMovie>()) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]

        if (NO_IMAGE != listItem.poster) {
            Glide.with(holder.image).load(listItem.poster).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    fun setItems(listItems: List<FavoriteMovie>) {
        this.listItems = listItems as MutableList<FavoriteMovie>
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}