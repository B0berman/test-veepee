package com.vp.favorites;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.vp.favorites.model.FavBean;
import com.vp.favorites.module.FavoriesAdapter;
import com.vp.list.GridPagingScrollListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView favorieView;
    private FavoriesAdapter listAdapter;
    private List<FavBean> list;
    private List<FavBean> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String addRequestString = getAddRequest(getIntent());
        boolean isAddRequest = addRequestString != null && "true".equals(addRequestString);

        if (isAddRequest)
        {
            AddFavorie add = new AddFavorie();
            add.save(this, getIntent());
            finish();
        }
        else {
            setContentView(R.layout.activity_favorite);
            favorieView = (RecyclerView) findViewById(R.id.favorieRecyclerView);

            list = new ArrayList<>();

            populaiteFavoriesList();
            listAdapter = new FavoriesAdapter(list, this);
            favorieView.setAdapter(listAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(this,
                    getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
            favorieView.setLayoutManager(layoutManager);
        }
    }

   private void populaiteFavoriesList () {
        File file = new File(this.getFilesDir(), getString(R.string.savedDataFile));
        if (!file.exists()) {
            list = new ArrayList<>();
            return;
        }
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            Map<String, FavBean> saved = (Map<String, FavBean>) stream.readObject();
            list = new ArrayList<>(saved.values());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String getAddRequest(Intent intent) {
        if (intent != null && intent.getData() != null)
        {
            return intent.getData().getQueryParameter ("addRequest");
        }
        return null;
    }
}
