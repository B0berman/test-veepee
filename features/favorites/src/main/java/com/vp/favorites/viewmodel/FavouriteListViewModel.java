package com.vp.favorites.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.uk.missionlabs.db.FavouritesDB;
import co.uk.missionlabs.db.Model.Favourite;
import co.uk.missionlabs.db.Model.ListItem;


public class FavouriteListViewModel extends ViewModel {
    FavouritesDB db = new FavouritesDB();
    private MutableLiveData<List<ListItem>> liveData = new MutableLiveData<>();

    @Inject
    FavouriteListViewModel() {

    }

    public LiveData<List<ListItem>> observeFavourites() {
        return liveData;
    }


    public void getFavourites() {
        ArrayList<ListItem> listItems = new ArrayList<>();
        ArrayList<Favourite> favourites = db.getFavourites();
        for(int i=0;i<favourites.size();i++){
            ListItem item = new ListItem();
            item.setItem(favourites.get(i).getMovieDetail().getTitle(),favourites.get(i).getMovieDetail().getYear(),favourites.get(i).getImdbID(),favourites.get(i).getMovieDetail().getPoster());
            listItems.add(item);
        }

        liveData.postValue(listItems);

    }
}
