package com.vp.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vp.list.model.ListItem;

import java.util.Collections;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private List<ListItem> listItems = Collections.emptyList();
    /* it's better like this(following The Android Profiler) - CE */
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        ListItem listItem = listItems.get(holder.getAdapterPosition());

        if (listItem.getPoster() != null && !NO_IMAGE.equals(listItem.getPoster())) {
            final float density = holder.image.getResources().getDisplayMetrics().density;
            GlideApp
                    .with(holder.image)
                    .load(listItem.getPoster())
                    .override((int) (300 * density), (int) (600 * density))
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setItems(List<ListItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    public void clearItems() {
        listItems.clear();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.poster);
        }

        @Override
        public void onClick(View v) {
            if(null != onItemClickListener) {
                onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
            }
            else {
                // Empty listener
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
