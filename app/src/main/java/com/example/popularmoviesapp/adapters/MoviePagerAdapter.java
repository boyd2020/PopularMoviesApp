package com.example.popularmoviesapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.popularmoviesapp.activities.MainActivity;
import com.example.popularmoviesapp.fragments.ReviewFragment;
import com.example.popularmoviesapp.fragments.TrailerFragment;

public class MoviePagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_SIZE = 2;
    private String movieId;


    public MoviePagerAdapter(FragmentManager fm, String movieId) {
        super(fm);
        this.movieId = movieId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.MOVIE_ID, movieId);
        Fragment fragment;

        switch (position)
        {
            case 0:
                fragment = new TrailerFragment();
                break;

            case 1:
                fragment = new ReviewFragment();
                break;

            default:
                fragment = new TrailerFragment();
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_SIZE;
    }
}
