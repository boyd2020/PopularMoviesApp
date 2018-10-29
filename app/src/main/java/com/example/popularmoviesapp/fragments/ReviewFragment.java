package com.example.popularmoviesapp.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.popularmoviesapp.activities.MainActivity;
import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.adapters.ReviewAdapter;
import com.example.popularmoviesapp.loaders.ReviewLoader;
import com.example.popularmoviesapp.model.Review;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Review>>{

    //Constants
    private static final int LOADER_ID = 3;

    //Views
    private RecyclerView recyclerView;
    private TextView emptyText;


    //Objects/Variables
    private ReviewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container,false);

        recyclerView = v.findViewById(R.id.recyclerView);
        emptyText = v.findViewById(R.id.emptyText);

        adapter = new ReviewAdapter(getContext(), new ArrayList<Review>());

        //Create Layout animation, add animation and adapter to RecyclerView
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_top);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(adapter);

        String movieId = getArguments().getString(MainActivity.MOVIE_ID);


        //Used to retrieve the network state
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //Used to verify if the device is connected to the internet
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(isConnected)
        {
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.MOVIE_ID, movieId);
            getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
        }
        else{
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(getString(R.string.no_connection));
        }

        return v;
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewLoader(getContext(), args.getString(MainActivity.MOVIE_ID));
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        if(!data.isEmpty())
            adapter.setCursor(data);
        else {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(getText(R.string.no_trailers_found));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
        adapter.setCursor(new ArrayList<Review>());

    }
}
