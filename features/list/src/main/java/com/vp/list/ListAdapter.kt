package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.ListAdapter.ListViewHolder
import com.vp.list.model.ListItem
import java.util.*

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {
    private var listItems = Collections.emptyList<ListItem>()
    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        if (listItem.poster != null && NO_IMAGE != listItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            GlideApp
                    .with(holder.image)
                    .load(listItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount() = listItems.size

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() = listItems.clear()

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener?:EMPTY_ON_ITEM_CLICK_LISTENER
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView
        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }
        override fun onClick(v: View) {
            onItemClickListener(listItems[adapterPosition].imdbID?:"")
        }
    }


    companion object {
        private const val NO_IMAGE = "N/A"
        private val EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener =  { _: String? -> }
    }
}

typealias OnItemClickListener = (String) -> Unit