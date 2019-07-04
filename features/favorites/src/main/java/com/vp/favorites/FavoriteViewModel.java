package com.vp.favorites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vp.detail.model.MovieDetail;
import com.vp.detail.utils.SharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private SharedPreference sharedPreference;

    private MutableLiveData<List<MovieDetail>> liveData = new MutableLiveData<>();

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        sharedPreference = new SharedPreference(application.getApplicationContext());
        getData();
    }


    public LiveData<List<MovieDetail>> getLiveData() {
        return liveData;
    }

    private List<MovieDetail> getData() {
        List<MovieDetail> data = new ArrayList<>();

        HashMap<String, MovieDetail> list = sharedPreference.getListId();

        if (list != null) {
            for (String i : list.keySet()) {
                data.add(list.get(i));
            }
        }
        liveData.setValue(data);

        return data;

    }


}
