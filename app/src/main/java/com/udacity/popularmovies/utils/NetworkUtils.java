package com.udacity.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Mihai on 3/12/2018.
 */

public class NetworkUtils {

    private static final String API_KEY = "your-api-key";
    private static final String API_ROOT = "http://api.themoviedb.org/3/";
    private static final String LIST_POPULARITY_URL = API_ROOT + "movie/popular?api_key=" + API_KEY;
    private static final String LIST_RATING_URL = API_ROOT + "movie/top_rated?api_key=" + API_KEY;
    public  static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static String getMoviesJson(boolean byPopularity) {
        String urlString = byPopularity ? LIST_POPULARITY_URL : LIST_RATING_URL;
        try {
            URL url = new URL(urlString);
            return getResponseFromHttpUrl(url);
        }catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
