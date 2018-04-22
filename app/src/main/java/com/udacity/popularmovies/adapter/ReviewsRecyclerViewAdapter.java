package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.MovieDetailActivity;
import com.udacity.popularmovies.MovieDetailFragment;
import com.udacity.popularmovies.MovieListActivity;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.utils.AndroidUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by Mihai on 4/20/2018.
 */

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {
    private final MovieDetailFragment mParentFragment;
    private List<Review> mValues;

    public ReviewsRecyclerViewAdapter(MovieDetailFragment parent) {
        mParentFragment = parent;
    }

    @Override
    public ReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_detail, parent, false);
        return new ReviewsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsRecyclerViewAdapter.ViewHolder holder, int position) {
        final Review review = mValues.get(position);

        holder.author.setText(review.getAuthor());

        String content = review.getContent()
                .replace("\n", "<br/>")
                .replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>")
                .replaceAll("_(.*?)_", "<i>$1</i>");

        holder.content.setText(Html.fromHtml(content));
        holder.content.setAnimationDuration(750L);
        holder.content.setInterpolator(new OvershootInterpolator());
        holder.content.setExpandInterpolator(new OvershootInterpolator());
        holder.content.setCollapseInterpolator(new OvershootInterpolator());


        SpannableString string = new SpannableString( mParentFragment.getString(R.string.read_more) );
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        holder.readMore.setText(string);
        holder.readMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                holder.content.expand();
            }
        });
        holder.content.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                holder.content.expand();
            }
        });

        ViewTreeObserver vto = holder.content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int readMoreVisibility = AndroidUtils.isTextViewEllipsized(holder.content) ? View.VISIBLE : View.INVISIBLE;
                holder.readMore.setVisibility(readMoreVisibility);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public void setData(List<Review> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView author;
        final ExpandableTextView content;
        final TextView readMore;

        ViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.review_author);
            content = (ExpandableTextView) view.findViewById(R.id.review_content);
            readMore = (TextView) view.findViewById(R.id.review_read_more);
        }
    }
}