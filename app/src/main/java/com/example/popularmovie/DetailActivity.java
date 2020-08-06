package com.example.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private ImageView mPosterShowImageView;
    private TextView mTitleTextView;
    private TextView mRateTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterShowImageView = (ImageView)findViewById(R.id.iv_show_poster);
        mTitleTextView = (TextView)findViewById(R.id.tv_movie_tittle);
        mRateTextView = (TextView)findViewById(R.id.tv_users_rating);
        mReleaseDateTextView =(TextView)findViewById(R.id.tv_release_date);
        mOverviewTextView = (TextView)findViewById(R.id.tv_overview);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        String rate = Double.toString(movie.getMovieRate());
        String date = movie.getMovieReleaseDate();
        String tittle = movie.getMovieTitle();
        String overview = movie.getMovieOverview();
        String posterUrl = MovieAdapter.getImageUri(movie);

        Picasso.with(this)
                .load(posterUrl)
                .error(R.drawable.ic_image_not_available)
                .into(mPosterShowImageView);

        mRateTextView.setText(rate);
        mReleaseDateTextView.setText(date);
        mTitleTextView.setText(tittle);
        mOverviewTextView.setText(overview);

    }
}
