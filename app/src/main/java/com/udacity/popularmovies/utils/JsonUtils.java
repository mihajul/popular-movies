package com.udacity.popularmovies.utils;

import android.util.Log;

import com.udacity.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";


    public static Movie parseSandwichJson(String json) {
        if (json != null) {
            try {
                Movie sandwich = new Movie();

                JSONObject jsonObj = new JSONObject(json);
                JSONObject nameObj = jsonObj.getJSONObject(NAME);

//                sandwich.setMainName(nameObj.getString(MAIN_NAME));
//
//                JSONArray aka = nameObj.getJSONArray(ALSO_KNOWN_AS);
//                sandwich.setAlsoKnownAs(new ArrayList<String>());
//                for (int i = 0; i < aka.length(); i++) {
//                    sandwich.getAlsoKnownAs().add(aka.getString(i));
//                }
//
//                sandwich.setPlaceOfOrigin(jsonObj.getString(PLACE_OF_ORIGIN));
//                sandwich.setDescription(jsonObj.getString(DESCRIPTION));
//                sandwich.setImage(jsonObj.getString(IMAGE));
//
//                JSONArray ingredients = jsonObj.getJSONArray(INGREDIENTS);
//                sandwich.setIngredients(new ArrayList<String>());
//                for (int i = 0; i < ingredients.length(); i++) {
//                    sandwich.getIngredients().add(ingredients.getString(i));
//                }

                return  sandwich;
            }catch (Exception e) {
                Log.e( LOG_TAG , e.getMessage());
            }
        }

        return null;
    }
}
