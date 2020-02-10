package com.vp.list

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listItems: List<ListItem> = emptyList()
    private var hasNextPage: Boolean = false
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun getItemViewType(position: Int) = when (position) {
        itemCount - 1 -> TYPE_LOADING
        else -> TYPE_LIST_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_LOADING -> LoaderViewHolder(parent)
        TYPE_LIST_ITEM -> ListViewHolder(parent)
        else -> throw IllegalArgumentException("unknown viewType : $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoaderViewHolder -> {
                // nothing to do
            }
            is ListViewHolder -> {
                val listItem = listItems[position]
                holder.bind(listItem)
                holder.itemView.setOnClickListener { onItemClickListener.onItemClick(listItem.imdbID) }
            }
        }
    }

    override fun getItemCount() = listItems.size + (if (hasNextPage) 1 else 0)

    fun setListItems(listItems: List<ListItem>, hasNextPage: Boolean) {
        this.listItems = listItems
        this.hasNextPage = hasNextPage
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems = emptyList()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener ?: EMPTY_ON_ITEM_CLICK_LISTENER
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        const val TYPE_LOADING = 1
        const val TYPE_LIST_ITEM = 2

        private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
            override fun onItemClick(imdbID: String) {
                //empty listener
            }
        }
    }
}

class LoaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false))

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
