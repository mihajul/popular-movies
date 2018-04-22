package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Mihai on 3/12/2018.
 */

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String originalTitle;
    private String imageUrl;
    private String overview;
    private double voteAverage;
    private Date releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(imageUrl);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        if(releaseDate != null) {
            dest.writeLong(releaseDate.getTime());
        }else {
            dest.writeLong(0);
        }
    }

    public Movie() {

    }

    private Movie(Parcel in){
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        imageUrl = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        long date = in.readLong();
        if(date != 0) {
            releaseDate = new Date(date);
        }
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
