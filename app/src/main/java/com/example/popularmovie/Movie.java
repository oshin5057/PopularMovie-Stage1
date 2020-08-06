package com.example.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private double mMovieRate;
    private String mMovieOverview;
    private String mMoviePoster;


    public Movie(String movieTitle, String movieReleaseDate, double movieRate, String movieOverview, String moviePoster){
        this.mMovieTitle = movieTitle;
        this.mMovieReleaseDate = movieReleaseDate;
        this.mMovieRate = movieRate;
        this.mMovieOverview = movieOverview;
        this.mMoviePoster = moviePoster;
    }

    public String getMovieTitle(){
        return mMovieTitle;
    }
    public String getMovieReleaseDate(){
        return mMovieReleaseDate;
    }
    public double getMovieRate(){
        return mMovieRate;
    }
    public String getMovieOverview(){
        return mMovieOverview;
    }
    public String getMoviePoster(){
        return mMoviePoster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieTitle);
        parcel.writeString(mMovieReleaseDate);
        parcel.writeDouble(mMovieRate);
        parcel.writeString(mMovieOverview);
        parcel.writeString(mMoviePoster);
    }

    public Movie(Parcel in) {
        mMovieTitle = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieRate = in.readDouble();
        mMovieOverview = in.readString();
        mMoviePoster = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
