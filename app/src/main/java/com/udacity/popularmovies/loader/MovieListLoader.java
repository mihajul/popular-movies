package com.udacity.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;
import com.udacity.popularmovies.utils.PreferencesUtils;

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
            boolean sortByPopularity = PreferencesUtils.getSort(getContext()) == PreferencesUtils.SORT_BY_POPULARITY;
            String json = NetworkUtils.getMoviesJson(sortByPopularity);
            List<Movie> movies = JsonUtils.parseMoviesJson(json);
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(movies);
    }
}
