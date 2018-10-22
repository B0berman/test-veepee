package com.vp.favorites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vp.favorites.model.FavBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddFavorie{

    public void save(Context context, Intent intent) {
        if (getMovieId(intent) != null) {
            saveIdToFile (intent, context);
        }
        Intent backIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID="+getMovieId(intent)));
        backIntent.setPackage(context.getPackageName());
        context.startActivity(backIntent);
    }

    private void saveIdToFile(Intent intent, Context context) {
        File file = new File(context.getFilesDir(), context.getString(R.string.savedDataFile));
        ObjectInputStream iStream = null;
        ObjectOutputStream oStream = null;
        try {
            Map<String, FavBean> saved;
            if (!file.exists()) {
                file.createNewFile();
                saved = new HashMap<>();
            }
            else
            {
                iStream = new ObjectInputStream(new FileInputStream(file));
                saved = (Map<String, FavBean>) iStream.readObject();
            }

            saved.put(getMovieId(intent), new FavBean(getMovieId(intent), getPoster(intent)));
            file.delete();
            file.createNewFile();
            oStream = new ObjectOutputStream(new FileOutputStream(file));
            oStream.writeObject(saved);
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {

                }
            }
            if (oStream != null) {
                try {
                    oStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private String getMovieId(Intent intent) {
        if (intent != null && intent.getData() != null)
        {
            return intent.getData().getQueryParameter ("imdbID");
        }
        return null;
    }
    private String getPoster(Intent intent) {
        if (intent != null && intent.getData() != null)
        {
            return intent.getData().getQueryParameter ("poster");
        }
        return null;
    }
}
