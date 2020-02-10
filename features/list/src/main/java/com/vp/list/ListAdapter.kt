package com.vp.list

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {
    var listItems: MutableList<ListItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(parent)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        holder.bind(listItem)
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(listItem.imdbID) }
    }

    override fun getItemCount() = listItems.size

    fun clearItems() = listItems.clear()

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener ?: EMPTY_ON_ITEM_CLICK_LISTENER
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
            override fun onItemClick(imdbID: String) {
                //empty listener
            }
        }
    }
}

class ListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)) {
    private val image: ImageView = itemView.findViewById(R.id.poster)

    fun bind(listItem: ListItem) {
        val poster = listItem.poster
        if (poster != null && NO_IMAGE != poster) {
            GlideApp
                    .with(image)
                    .load(poster)
                    .override((300 * DENSITY).toInt(), (600 * DENSITY).toInt())
                    .into(image)
        } else {
            image.setImageResource(R.drawable.placeholder)
        }
    }

    companion object {
        private val DENSITY = Resources.getSystem().displayMetrics.density
        private const val NO_IMAGE = "N/A"
    }
}
