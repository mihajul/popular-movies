package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.MovieDetailActivity;
import com.udacity.popularmovies.MovieDetailFragment;
import com.udacity.popularmovies.MovieListActivity;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.dummy.DummyContent;

import java.util.List;

/**
 * Created by Mihai on 3/12/2018.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final MovieListActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;

    public MovieRecyclerViewAdapter(MovieListActivity parent,
                                    List<DummyContent.DummyItem> items,
                                    boolean twoPane) {
        mValues = items;
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
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                                                   if (mTwoPane) {
                                                       Bundle arguments = new Bundle();
                                                       arguments.putString(MovieDetailFragment.ARG_ITEM_ID, item.id);
                                                       MovieDetailFragment fragment = new MovieDetailFragment();
                                                       fragment.setArguments(arguments);
                                                       mParentActivity.getSupportFragmentManager().beginTransaction()
                                                               .replace(R.id.movie_detail_container, fragment)
                                                               .commit();
                                                   } else {
                                                       Context context = view.getContext();
                                                       Intent intent = new Intent(context, MovieDetailActivity.class);
                                                       intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, item.id);

                                                       context.startActivity(intent);
                                                   }
                                               }
                                           }
        );
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
