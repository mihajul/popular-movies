package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;

import java.util.List;

/**
 * Created by Mihai on 4/20/2018.
 */

public class ReviewsGridAdapter extends BaseAdapter {
    private Context context;
    private List<Review> reviews;

    public ReviewsGridAdapter(Context context) {
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.review_detail, null);

            Review review = reviews.get(position);

            TextView author = (TextView) gridView.findViewById(R.id.review_author);
            author.setText(review.getAuthor());

            TextView content = (TextView) gridView.findViewById(R.id.review_content);
            content.setText(review.getContent());
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    public void setData(List<Review> data) {
        reviews = data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return reviews == null ? 0 : reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}