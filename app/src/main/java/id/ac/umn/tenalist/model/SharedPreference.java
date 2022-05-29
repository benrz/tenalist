package id.ac.umn.tenalist.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SharedPreference {

    public static final String PREFS_NAME = "Bazaar";
    public static final String FAVORITES = "Bazaar_Favorites";

    public SharedPreference() {
        super();
    }

    public void saveFavorites(Context context, List<EventListModel> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, EventListModel bazaar) {
        List<EventListModel> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<EventListModel>();
        favorites.add(bazaar);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, EventListModel bazaar) {
        ArrayList<EventListModel> favorites = getFavorites(context);
        if (favorites != null) {
            Log.d("DEBUG", "NAMA BAZAAR");
//            favorites.remove(1);
            Log.d("DEBUG", bazaar.getEventTitle());
//            favorites.remove(bazaar);
            for (int i=0; i<favorites.size();i++){
                if (favorites.get(i).getEventId() == bazaar.getEventId()) {
                    Log.d("DEBUG", "ada yang sama");
                    favorites.remove(i);
                }
            }
            for (int i=0; i<favorites.size();i++){
                Log.d("DEBUG", favorites.get(i).getEventTitle());
            }
            saveFavorites(context, favorites);
        }

    }

//    public ArrayList<BazaarDetail.bazaarDetail> getFavorites(Context context) {
//        SharedPreferences settings;
//        List<BazaarDetail.bazaarDetail> favorites;
//
//        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//
//        if (settings.contains(FAVORITES)) {
//            String jsonFavorites = settings.getString(FAVORITES, null);
//            Gson gson = new Gson();
//            BazaarDetail.bazaarDetail[] favoriteItems = gson.fromJson(jsonFavorites,
//                    BazaarDetail.bazaarDetail[].class);
//
//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<BazaarDetail.bazaarDetail>(favorites);
//        } else
//            return null;
//
//        return (ArrayList<BazaarDetail.bazaarDetail>) favorites;
//    }

    public ArrayList<EventListModel> getFavorites(Context context) {
        SharedPreferences settings;
        List<EventListModel> favorites;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            EventListModel[] favoriteItems = gson.fromJson(jsonFavorites,
                    EventListModel[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<EventListModel>(favorites);
        } else
            return null;

        return (ArrayList<EventListModel>) favorites;
    }

}
