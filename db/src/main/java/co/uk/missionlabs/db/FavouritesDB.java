package co.uk.missionlabs.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.uk.missionlabs.db.Model.Favourite;
import co.uk.missionlabs.db.Model.Favourites;

public class FavouritesDB {

    //this is quick and dirty - in the real world I'd use a database like Realm or Room

    private static ArrayList<Favourite> favourites;
    public boolean isFavourite(String id){
        if(favourites==null || favourites.size()==0){
            return false;
        }

        for(int i=0;i<favourites.size();i++){
            if(favourites.get(i).getImdbID().contentEquals(id)){
                return true;
            }
        }
        return false;
    }

    public void toggleFavourite(Favourite favourite){
        if(isFavourite(favourite.getImdbID())){
            removedFavourite(favourite.getImdbID());
        }else{
            addFavourite(favourite);
        }
    }

    public void addFavourite(Favourite favourite){
        if(favourites==null){
            favourites = new ArrayList<>();
        }

        favourites.add(favourite);
    }

    public void removedFavourite(String id){
        for(int i=0;i<favourites.size();i++){
            if(favourites.get(i).getImdbID().contentEquals(id)){
                favourites.remove(i);
            }
        }
    }

    public Favourite getFavourite(String id){
        for(int i=0;i<favourites.size();i++){
            if(favourites.get(i).getImdbID().contentEquals(id)){
                return favourites.get(i);
            }
        }

        return null;
    }

    public ArrayList<Favourite> getFavourites(){
        ArrayList<Favourite> results = new ArrayList<>();
        if(favourites == null){
            return results;
        }

        results.addAll(favourites);
        return results;
    }

    public static void saveDB(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences("database", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Favourites favDB = new Favourites();
        favDB.favourites = favourites;
        editor.putString("favourites",gson.toJson(favDB));
        editor.commit();
    }

    public static void loadDB(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences("database", Context.MODE_PRIVATE);
        String flatFile = sharedPref.getString("favourites",null);
        if(flatFile!=null){
            Favourites favDB = gson.fromJson(flatFile,Favourites.class);
            favourites = favDB.favourites;
        }
    }


}
