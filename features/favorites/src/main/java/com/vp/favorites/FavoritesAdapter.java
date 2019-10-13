package com.vp.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.vp.detail.model.MovieDetail;
import com.vp.list.GlideApp;

import java.util.Collections;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ListViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private List<MovieDetail> listItems = Collections.emptyList();
    private OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = new OnItemClickListener() {
        @Override
        public void onItemClick(String imdbID) {
            //empty listener
        }
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MovieDetail listItem = listItems.get(position);

        if (!NO_IMAGE.equals(listItem.getPoster())) {
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

    void setItems(List<MovieDetail> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
        }
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
            onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
