package com.vp.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItems: MutableList<ListItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER_VIEW) {
            return FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_footer, parent, false))
        }
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
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
        }
    }

    override fun getItemCount(): Int {
        return if (listItems.size == 0) {
            1
        } else {
            listItems.size + 1
        }
    }

    fun setItems(listItems: MutableList<ListItem>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listItems.size) {
            FOOTER_VIEW
        } else super.getItemViewType(position)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = when {
            onItemClickListener != null -> onItemClickListener
            else -> EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[adapterPosition].imdbID)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }

    companion object {
        private const val FOOTER_VIEW = 1
        private const val NO_IMAGE = "N/A"
        private val EMPTY_ON_ITEM_CLICK_LISTENER = object : OnItemClickListener {
            override fun onItemClick(imdbID: String?) {

            }
        }
    }
}
