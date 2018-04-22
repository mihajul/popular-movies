package com.udacity.popularmovies;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.adapter.ReviewsRecyclerViewAdapter;
import com.udacity.popularmovies.adapter.TrailersRecyclerViewAdapter;
import com.udacity.popularmovies.data.MovieContract;
import com.udacity.popularmovies.loader.ReviewsLoader;
import com.udacity.popularmovies.loader.TrailersLoader;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_MOVIE = "movie";
    public static final SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("YYYY");
    public static final SimpleDateFormat RELEASE_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");

    private Movie item;

    private static final int REVIEWS_LOADER_ID = 1;
    private static final int TRAILERS_LOADER_ID = 2;

    private RecyclerView reviewsRecyclerView;
    private ProgressBar mLoadingIndicatorReviews;
    private TextView mErrorMessageDisplayReviews;

    private RecyclerView trailersRecyclerView;
    private ProgressBar mLoadingIndicatorTrailers;
    private TextView mErrorMessageDisplayTrailers;

    ImageButton favoriteButton;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            item = getArguments().getParcelable(ARG_MOVIE);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupReviewsList();
        setupTrailersList();
    }

    private void setupReviewsList() {
        mLoadingIndicatorReviews = (ProgressBar) getView().findViewById(R.id.pb_loading_indicator_reviews);
        mErrorMessageDisplayReviews = (TextView) getView().findViewById(R.id.tv_error_message_reviews);

        reviewsRecyclerView = (RecyclerView) getView().findViewById(R.id.reviews_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRecyclerView.setLayoutManager(mLayoutManager);
        reviewsRecyclerView.setAdapter(new ReviewsRecyclerViewAdapter(this));

        getActivity().getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, new ReviewCallbacks());
    }


    private class ReviewCallbacks implements LoaderManager.LoaderCallbacks<List<Review>> {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new ReviewsLoader(MovieDetailFragment.this.getContext(), mLoadingIndicatorReviews, item.getId());
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            mLoadingIndicatorReviews.setVisibility(View.GONE);
            ((ReviewsRecyclerViewAdapter) reviewsRecyclerView.getAdapter()).setData(data);

            if (null == data) {
                showErrorMessage(getString(R.string.error_message_reviews));
            } else if (data.isEmpty()) {
                showErrorMessage(getString(R.string.no_reviews));
            } else {
                showGridView();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }

        private void showGridView() {
            mErrorMessageDisplayReviews.setVisibility(View.GONE);
            reviewsRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(String message) {
            reviewsRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplayReviews.setVisibility(View.VISIBLE);
            mErrorMessageDisplayReviews.setText(message);
        }
    }


    private void setupTrailersList() {
        mLoadingIndicatorTrailers = (ProgressBar) getView().findViewById(R.id.pb_loading_indicator_trailers);
        mErrorMessageDisplayTrailers = (TextView) getView().findViewById(R.id.tv_error_message_trailers);

        trailersRecyclerView = (RecyclerView) getView().findViewById(R.id.trailers_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailersRecyclerView.setLayoutManager(mLayoutManager);
        trailersRecyclerView.setAdapter(new TrailersRecyclerViewAdapter(this));

        getActivity().getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, new TrailerCallbacks());
    }

    private class TrailerCallbacks implements LoaderManager.LoaderCallbacks<List<Trailer>> {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailersLoader(MovieDetailFragment.this.getContext(), mLoadingIndicatorReviews, item.getId());
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            mLoadingIndicatorTrailers.setVisibility(View.GONE);
            ((TrailersRecyclerViewAdapter) trailersRecyclerView.getAdapter()).setData(data);

            if (null == data) {
                showErrorMessage(getString(R.string.error_message_trailers));
            } else if (data.isEmpty()) {
                showErrorMessage(getString(R.string.no_trailers));
            } else {
                showGridView();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }

        private void showGridView() {
            mErrorMessageDisplayTrailers.setVisibility(View.GONE);
            trailersRecyclerView.setVisibility(View.VISIBLE);
        }

        private void showErrorMessage(String message) {
            trailersRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplayTrailers.setVisibility(View.VISIBLE);
            mErrorMessageDisplayTrailers.setText(message);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        TextView overview = (TextView) rootView.findViewById(R.id.movie_detail);
        TextView originalTitle = (TextView) rootView.findViewById(R.id.tv_original_title);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        TextView voteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        ImageView poster = (ImageView) rootView.findViewById(R.id.iv_poster);
        favoriteButton = (ImageButton) rootView.findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(this);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);

        if (item != null) {
            overview.setText(item.getOverview());
            originalTitle.setText(item.getOriginalTitle());
            if(item.getReleaseDate()!=null) {
                releaseDate.setText(YEAR_DATE_FORMAT.format(item.getReleaseDate()));
            }
            voteAverage.setText(item.getVoteAverage() + "/10");
            setFavoriteIconColor(item.isFavorite());

            int width = (int) getResources().getDimension(R.dimen.column_width);
            int height = (int) (width * 1.5);

            poster.getLayoutParams().height = height;
            poster.getLayoutParams().width = width;

            Picasso.with(getContext())
                    .load(NetworkUtils.IMAGE_BASE_URL + item.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .resize(width, height)
                    .into(poster);

            if (appBarLayout != null) {
                appBarLayout.setTitle(item.getTitle());
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_button:
                onFavoriteClick();
                break;
        }
    }

    public void onFavoriteClick() {


        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_LONG).show();
            }
        };


        if(item.isFavorite()) {

            Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, item.getId());
            queryHandler.startDelete(0,null, uri, null, null);
            item.setFavorite(false);

        }else {

            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, item.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, item.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, item.getOriginalTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL, item.getImageUrl());
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, item.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, RELEASE_DATE_FORMAT.format(item.getReleaseDate()));
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, item.getVoteAverage());

            queryHandler.startInsert(0, null, MovieContract.MovieEntry.CONTENT_URI, contentValues);
            item.setFavorite(true);

        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra(MovieListActivity.MOVIE_RESULT_KEY, item);
        getActivity().setResult(Activity.RESULT_OK,returnIntent);

        setFavoriteIconColor(item.isFavorite());
    }

    private void setFavoriteIconColor(boolean favorite) {
        if(favorite) {
            favoriteButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
        }else {
            favoriteButton.setColorFilter(null);
        }
    }

}
