package com.udacity.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

/**
 * Created by Mihai on 3/13/2018.
 */

public class TrailersLoader extends AsyncTaskLoader<List<Trailer>> {
    List<Trailer> trailers = null;
    ProgressBar mLoadingIndicator;
    Integer movieId;

    public TrailersLoader(Context context, ProgressBar mLoadingIndicator, Integer movieId) {
        super(context);
        this.mLoadingIndicator = mLoadingIndicator;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (trailers != null) {
            deliverResult(trailers);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }


    @Override
    public List<Trailer> loadInBackground() {

        if(!NetworkUtils.isOnline(getContext())) {
            return null;
        }
        try {
            String json = NetworkUtils.getTrailersJson(movieId);
            List<Trailer> trailers = JsonUtils.parseTrailersJson(json);
            return trailers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deliverResult(List<Trailer> data) {
        trailers = data;
        super.deliverResult(data);
    }
}
