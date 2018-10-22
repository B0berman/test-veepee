package com.vp.favorites.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vp.favorites.R;
import com.vp.favorites.model.FavBean;
import com.vp.list.GlideApp;

import java.util.List;

public class FavoriesAdapter  extends RecyclerView.Adapter<FavoriesAdapter.ListViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private List<FavBean> listItems;
    private Context context;

    public FavoriesAdapter(List<FavBean> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        FavBean listItem = listItems.get(position);

        if (listItem != null && listItem.getImdbID() != null) {
            holder.title.setText(listItem.getImdbID());
            final float density = holder.image.getResources().getDisplayMetrics().density;
            GlideApp
                    .with(holder.image)
                    .load(listItem.getPoster())
                    .override((int) (300 * density), (int) (600 * density))
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.placeholder);
            holder.title.setText("No Data");
        }
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void clearItems() {
        listItems.clear();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID="+title.getText()));
            intent.setPackage(context.getPackageName());
            context.startActivity(intent);
        }
    }

}
