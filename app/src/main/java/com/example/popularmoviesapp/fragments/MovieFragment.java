package com.example.popularmoviesapp.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.adapters.MovieAdapter;
import com.example.popularmoviesapp.loaders.MovieLoader;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.example.popularmoviesapp.activities.MainActivity.KEY_PATH;

public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    //Constants
    private static final int LOADER_ID = 1;
    private static final int OFFSET = 0;
    private static final String SCROLL_POS = "scrollPosition";

    //Views
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private TextView emptyText;

    //Variables
    private int scrollPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_fragment, container, false);

        //Initialize Views
        recyclerView = v.findViewById(R.id.recyclerView);
        emptyText = v.findViewById(R.id.emptyText);

        //Initialize Movie Adapter
        adapter = new MovieAdapter(getContext(), new ArrayList<Movie>());

        //Create Layout animation, add animation and adapter to RecyclerView
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_top);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.setAdapter(adapter);

        //Scroll to previous position if fragment orientation is changed
        if(savedInstanceState != null)
            scrollPosition = savedInstanceState.getInt(SCROLL_POS);
        else
            scrollPosition = 0;


        //Retrieve key path
        String keyPath = MovieUtil.getMovieUrl(getArguments().getString(KEY_PATH, MovieUtil.PATH_POPULAR));


        //Used to retrieve the network state
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //Used to verify if the device is connected to the internet
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(isConnected)
        {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_PATH, keyPath);
            getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
            emptyText.setVisibility(View.GONE);
        }
        else{
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(getString(R.string.no_connection));
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POS, ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle bundle) {
        return new MovieLoader(getContext(), bundle.getString(KEY_PATH));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        adapter.setCursor(data);
        recyclerView.startLayoutAnimation();
        ((GridLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollPosition, OFFSET);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.setCursor(new ArrayList<Movie>());
    }

}
