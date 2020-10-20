package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vp.list.ListAdapter.BaseListViewHolder
import com.vp.list.R.drawable
import com.vp.list.R.id
import com.vp.list.R.layout
import com.vp.list.model.ListItem

class ListAdapter : Adapter<BaseListViewHolder>() {
    private var listItems = mutableListOf<ListItem>()
    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
    private var isLoadingMore = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOADING_MORE) {
            LoadingMoreViewHolder(
                inflater.inflate(
                    layout.item_loading_more,
                    parent,
                    false
                )
            )
        } else {
            ListViewHolder(inflater.inflate(layout.item_list, parent, false))
        }
    }

    private fun isLoadingMoreItem(position: Int): Boolean {
        return isLoadingMore && position == listItems.size
    }

    override fun onBindViewHolder(holder: BaseListViewHolder, position: Int) {
        if (!isLoadingMoreItem(position)) {
            val listItem = listItems[position]
            val listViewHolder = holder as ListViewHolder
            if (listItem.poster != null && listItem.poster != NO_IMAGE) {
                val density = listViewHolder.image.resources.displayMetrics.density
                GlideApp
                    .with(listViewHolder.image)
                    .load(listItem.poster)
                    .override((300 * density).toInt(), (600 * density).toInt())
                    .into(listViewHolder.image)
            } else {
                listViewHolder.image.setImageResource(drawable.placeholder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingMoreItem(position)) {
            VIEW_TYPE_LOADING_MORE
        } else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size + if (isLoadingMore) 1 else 0
    }

    fun setItems(listItems: List<ListItem>, isLoadingMore: Boolean) {
        this.listItems.clear()
        this.listItems.addAll(listItems)
        this.isLoadingMore = isLoadingMore
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    fun getItemSpanSize(position: Int, defaultSpanCount: Int): Int {
        return if (isLoadingMoreItem(position)) defaultSpanCount else 1
    }

    abstract class BaseListViewHolder(itemView: View) : ViewHolder(itemView)

    internal inner class ListViewHolder(itemView: View) : BaseListViewHolder(itemView), OnClickListener {
        var image: ImageView
        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(id.poster)
        }
    }

    internal class LoadingMoreViewHolder(itemView: View) : BaseListViewHolder(itemView)
    fun interface OnItemClickListener {
        fun onItemClick(imdbID: String)
    }

    companion object {
        private const val NO_IMAGE = "N/A"
        private const val VIEW_TYPE_LOADING_MORE = 1
        private val EMPTY_ON_ITEM_CLICK_LISTENER = OnItemClickListener { /* no-op */ }
    }
}
