package com.udacity.popularmovies;

import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.text.SimpleDateFormat;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie";
    public static final SimpleDateFormat yearDateFormat = new SimpleDateFormat("YYYY");
    private Movie item;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        TextView overview = (TextView) rootView.findViewById(R.id.movie_detail);
        TextView originalTitle = (TextView) rootView.findViewById(R.id.tv_original_title);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        TextView voteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        ImageView poster = (ImageView) rootView.findViewById(R.id.iv_poster);
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);

        if (item != null) {
            overview.setText(item.getOverview());
            originalTitle.setText(item.getOriginalTitle());
            if(item.getReleaseDate()!=null) {
                releaseDate.setText(yearDateFormat.format(item.getReleaseDate()));
            }
            voteAverage.setText(item.getVoteAverage() + "/10");

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
}
