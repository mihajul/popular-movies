package com.udacity.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.MovieDetailFragment;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.utils.AndroidUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by Mihai on 4/20/2018.
 */

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {
    private final MovieDetailFragment mParentFragment;
    private List<Trailer> mValues;

    public TrailersRecyclerViewAdapter(MovieDetailFragment parent) {
        mParentFragment = parent;
    }

    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_detail, parent, false);
        return new TrailersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailersRecyclerViewAdapter.ViewHolder holder, int position) {
        final Trailer trailer = mValues.get(position);

        Picasso.with(mParentFragment.getContext())
                .load(String.format(NetworkUtils.TRAILER_IMAGE_URL, trailer.getKey()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                               playTrailer(trailer.getKey());
                                               }
                                          }
        );
    }

    private void playTrailer(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(NetworkUtils.TRAILER_VIDEO_URL,id)));
        try {
            mParentFragment.getContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mParentFragment.getContext().startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setData(List<Trailer> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;

        ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.trailer_image);
        }
    }
}