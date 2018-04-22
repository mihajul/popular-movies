package com.udacity.popularmovies.utils;

import android.util.Log;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";


   public static List<Movie> parseMoviesJson(String json) {

       final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("YYYY-MM-DD");

       if (json != null) {
            List<Movie> movies = new ArrayList<>();
            try {

                JSONObject jsonObj = new JSONObject(json);
                JSONArray results = jsonObj.getJSONArray(RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObj = results.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(movieObj.getInt(ID));
                    movie.setImageUrl(movieObj.getString(POSTER_PATH));
                    movie.setTitle(movieObj.getString(TITLE));
                    movie.setOriginalTitle(movieObj.getString(ORIGINAL_TITLE));
                    movie.setVoteAverage(movieObj.getDouble(VOTE_AVERAGE));
                    movie.setOverview(movieObj.optString(OVERVIEW,"").replaceAll("\\r\\n?","\n"));

                    String date = movieObj.getString(RELEASE_DATE);
                    if(date != null && !date.isEmpty()) {
                        movie.setReleaseDate(DATE_PARSER.parse(date));
                    }
                    movies.add(movie);
                }
                return  movies;
            }catch (Exception e) {
                Log.e( LOG_TAG , e.getMessage());
            }
        }

        return null;
    }

    public static List<Trailer> parseTrailersJson(String json) {
        if (json != null) {
            List<Trailer> trailers = new ArrayList<>();
            try {

                JSONObject jsonObj = new JSONObject(json);
                JSONArray results = jsonObj.getJSONArray(RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject trailerObj = results.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.setId(trailerObj.getString(ID));
                    trailer.setKey(trailerObj.getString(KEY));
                    trailer.setName(trailerObj.getString(NAME));
                    trailers.add(trailer);
                }
                return  trailers;
            }catch (Exception e) {
                Log.e( LOG_TAG , e.getMessage());
            }
        }

        return null;
    }

    public static List<Review> parseReviewsJson(String json) {
        if (json != null) {
            List<Review> reviews = new ArrayList<>();
            try {

                JSONObject jsonObj = new JSONObject(json);
                JSONArray results = jsonObj.getJSONArray(RESULTS);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject trailerObj = results.getJSONObject(i);
                    Review review = new Review();
                    review.setId(trailerObj.getString(ID));
                    review.setAuthor(trailerObj.getString(AUTHOR));
                    review.setContent(trailerObj.getString(CONTENT));
                    reviews.add(review);
                }
                return  reviews;
            }catch (Exception e) {
                Log.e( LOG_TAG , e.getMessage());
            }
        }

        return null;
    }
}
