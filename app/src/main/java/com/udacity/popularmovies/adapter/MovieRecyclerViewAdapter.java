package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.MovieDetailActivity;
import com.udacity.popularmovies.MovieDetailFragment;
import com.udacity.popularmovies.MovieListActivity;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

/**
 * Created by Mihai on 3/12/2018.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final MovieListActivity mParentActivity;
    private List<Movie> mValues;
    private final boolean mTwoPane;

    public MovieRecyclerViewAdapter(MovieListActivity parent,
                                    boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = mValues.get(position);

        int width = (int) mParentActivity.getResources().getDimension(R.dimen.column_width);
                //AndroidUtils.convertDpToPixel(MovieListActivity.COLUMN_WIDTH, mParentActivity);
        int height = (int) ((double)width * 1.5);
        Picasso.with(mParentActivity)
                .load(NetworkUtils.IMAGE_BASE_URL + movie.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .resize(width, height)
                .centerCrop()
                .into(holder.mContentView , new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mTextView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.mTextView.setVisibility(View.VISIBLE);
                    }
                });
        holder.mTextView.setText(movie.getTitle());
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   showDetailActivity(view, movie);
                                               }
                                           }
        );
    }

    private void showDetailActivity(View view, Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Context context = view.getContext();
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setData(List<Movie> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mContentView;
        final TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mContentView = (ImageView) view.findViewById(R.id.content);
            mTextView = (TextView) view.findViewById(R.id.title);
        }
    }
}
