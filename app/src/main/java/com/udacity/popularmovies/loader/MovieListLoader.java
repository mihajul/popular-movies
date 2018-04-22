package com.udacity.popularmovies.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.popularmovies.MovieDetailFragment;
import com.udacity.popularmovies.data.MovieContract;
import com.udacity.popularmovies.data.MovieContract.MovieEntry;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;
import com.udacity.popularmovies.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai on 3/13/2018.
 */

public class MovieListLoader extends AsyncTaskLoader<List<Movie>> {
    List<Movie> movies = null;
    ProgressBar mLoadingIndicator;

    public MovieListLoader(Context context, ProgressBar mLoadingIndicator) {
        super(context);
        this.mLoadingIndicator = mLoadingIndicator;
    }

    @Override
    protected void onStartLoading() {
        if (movies != null) {
            deliverResult(movies);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }


    @Override
    public List<Movie> loadInBackground() {

        if(!NetworkUtils.isOnline(getContext())) {
            return null;
        }
        try {
            List<Movie> favorites = getFavorites();

            boolean sortByFavorites = PreferencesUtils.getSort(getContext()) == PreferencesUtils.SORT_BY_FAVORITES;
            if(sortByFavorites) {
                return favorites;
            }

            boolean sortByPopularity = PreferencesUtils.getSort(getContext()) == PreferencesUtils.SORT_BY_POPULARITY;
            String json = NetworkUtils.getMoviesJson(sortByPopularity);
            List<Movie> movies = JsonUtils.parseMoviesJson(json);

            if(movies != null) {
                for (Movie movie : movies) {
                    for (Movie favorite : favorites) {
                        if (favorite.getId() == movie.getId()) {
                            movie.setFavorite(true);
                        }
                    }
                }
            }


            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Movie> getFavorites() {
        Cursor cursor = getContext().getContentResolver().query(MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        List<Movie> movies= new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();

            try {
                movie.setFavorite(true);
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setImageUrl(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_IMAGE_URL)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
                movie.setVoteAverage(cursor.getDouble( cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setReleaseDate(MovieDetailFragment.RELEASE_DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE))));
            }catch(Exception e) {
                e.printStackTrace();
            }
            movies.add(movie);
        }
        cursor.close();
        return movies;
    }

    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(movies);
    }
}
