package com.udacity.popularmovies;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.udacity.popularmovies.adapter.MovieRecyclerViewAdapter;
import com.udacity.popularmovies.loader.MovieListLoader;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.PreferencesUtils;

import java.util.List;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private static final int LOADER_ID = 0;
    //public static final int COLUMN_WIDTH = 160;
    private static final String TAG = MovieListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        recyclerView = (RecyclerView) findViewById(R.id.movie_list);

        setupRecyclerView((RecyclerView) recyclerView);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new MovieRecyclerViewAdapter(this,  mTwoPane));
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new MovieListLoader(this, mLoadingIndicator);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        ((MovieRecyclerViewAdapter) recyclerView.getAdapter()).setData(data);

        if (null == data) {
            showErrorMessage();
        } else {
            showGridView();
        }
    }

    private void showGridView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, MovieListActivity.this);
            return true;
        }
        else if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.sortby_dialog);
        dialog.show();

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        int radioId =  R.id.sort_by_rating;
        if(PreferencesUtils.getSort(this) == PreferencesUtils.SORT_BY_POPULARITY) {
            radioId = R.id.sort_by_popularity;
        }
        rg.check(radioId);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int sort = PreferencesUtils.SORT_BY_RATING;
                if(checkedId == R.id.sort_by_popularity) {
                    sort = PreferencesUtils.SORT_BY_POPULARITY;
                }
                PreferencesUtils.setSort(MovieListActivity.this, sort);
                dialog.hide();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, MovieListActivity.this);
    }
}
