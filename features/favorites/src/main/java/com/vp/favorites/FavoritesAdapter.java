package com.vp.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vp.detail.model.MovieDetailRealm;

import java.util.ArrayList;
import java.util.Collections;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ListViewHolder> {

    private static final String NO_IMAGE = "N/A";
    private Context context;
    private ArrayList<MovieDetailRealm> favoritesList;

    FavoritesAdapter(Context context, ArrayList<MovieDetailRealm> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MovieDetailRealm favoriteMovie = favoritesList.get(position);

        if (favoriteMovie.getPoster() != null && !NO_IMAGE.equals(favoriteMovie.getPoster())) {
            Picasso.with(context)
                    .load(favoriteMovie.getPoster())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.poster);
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }

}
