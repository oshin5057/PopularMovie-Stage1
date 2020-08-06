package com.example.popularmovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>
        ,MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ImageView mImageView;
    private TextView mNoInternetTextView;
    private ProgressBar mLoadingIndicator;

    private String mShortByParam = "popular";

    private NetworkInfo networkInfo;

    private static final int MOVIE_LOADER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movie);
        mImageView = (ImageView)findViewById(R.id.iv_no_internet);
        mNoInternetTextView= (TextView)findViewById(R.id.tv_no_internet);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator);

        mAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager.LoaderCallbacks<List<Movie>> callbacks = MainActivity.this;
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callbacks);
        }
        else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            List<Movie> mMovies = null;

            @Override
            protected void onStartLoading(){
                if (mMovies != null){
                    deliverResult(mMovies);
                }
                else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            @Nullable
            @Override
            public List<Movie> loadInBackground() {

                URL url = NetworkUtils.buildUrl(mShortByParam);

                String jsonMovieResponse = null;
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(url);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                List<Movie> simpleMovieJsonData;
                try {
                    simpleMovieJsonData = OpenMovieJsonUtils.getMovieListFromJson(jsonMovieResponse);
                    return simpleMovieJsonData;
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            }

            public void deliverResult(List<Movie> data){
                mMovies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(data);

        if (data == null){
            showErrorMessage();
        }
        else {
            showMovieData();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    void showMovieData(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {

        Intent openDetailActivityIntent = new Intent(MainActivity.this, DetailActivity.class);
        openDetailActivityIntent.putExtra("movie", clickedMovie);

        startActivity(openDetailActivityIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_by_most_popular){
            showMostPopularMovies();
        }
        if (id == R.id.action_sort_by_high_rated){
            showHighRatedMovies();
        }
        return true;
    }


    public void showMostPopularMovies(){
        if (networkInfo!= null && networkInfo.isConnected()){
            mAdapter.setMovieData(null);
            mShortByParam = "popular";
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
        else {
            mAdapter.setMovieData(null);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
    }

    public void showHighRatedMovies(){
        if (networkInfo!= null && networkInfo.isConnected()){
            mAdapter.setMovieData(null);
            mShortByParam = "top_rated";
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
        else {
            mAdapter.setMovieData(null);
            mNoInternetTextView.setVisibility(View.VISIBLE);
        }
    }

}