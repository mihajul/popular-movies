package com.udacity.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;
import com.udacity.popularmovies.utils.PreferencesUtils;

import java.util.List;

/**
 * Created by Mihai on 3/13/2018.
 */

public class ReviewsLoader extends AsyncTaskLoader<List<Review>> {
    List<Review> reviews = null;
    ProgressBar mLoadingIndicator;
    Integer movieId;

    public ReviewsLoader(Context context, ProgressBar mLoadingIndicator, Integer movieId) {
        super(context);
        this.mLoadingIndicator = mLoadingIndicator;
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (reviews != null) {
            deliverResult(reviews);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }


    @Override
    public List<Review> loadInBackground() {

        if(!NetworkUtils.isOnline(getContext())) {
            return null;
        }
        try {
            String json = NetworkUtils.getReviewsJson(movieId);
            List<Review> reviews = JsonUtils.parseReviewsJson(json);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deliverResult(List<Review> data) {
        reviews = data;
        super.deliverResult(data);
    }
}
