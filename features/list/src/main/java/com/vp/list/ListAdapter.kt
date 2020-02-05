package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.glideaddons.GlideApp
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var listItems: MutableList<ListItem> = mutableListOf()
    private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
        override fun onItemClick(imdbID: String?) {
        }
    }
    var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems.get(position)
        if (listItem.poster != NO_IMAGE) {
            val density = holder.image.getResources().displayMetrics.density
            GlideApp
                .with(holder.image)
                .load(listItem.poster)
                .override((300 * density).toInt(), (600 * density).toInt())
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(listItems.get(adapterPosition).imdbID)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }

    companion object {
        private val NO_IMAGE: String? = "N/A"
    }
}