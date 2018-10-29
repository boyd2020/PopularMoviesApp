package com.example.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String author, content;

    public Review(){}

    private Review(Parcel in)
    {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>()
    {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
