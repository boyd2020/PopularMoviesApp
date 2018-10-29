package com.example.popularmoviesapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.example.popularmoviesapp.model.Movie;
import com.example.popularmoviesapp.model.Review;
import com.example.popularmoviesapp.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by boyd on 5/21/2018.
 */

public class MovieUtil {

    //Urls and Keys Values
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String VIDEO_URL = "https://www.youtube.com";
    private static final String API_KEY  = "1d3c784d7ba3beed587ab39328870c22";
    private static final String LANGUAGE = "en-US";
    private static final String PAGE = "1";

    //Path Values
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_REVIEW = "reviews";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_WATCH = "watch";

    //Key Names
    private static final String QUERY_PARAM_VIDEO="v";
    private static final String QUERY_PARAM_API_KEY = "api_key";
    private static final String QUERY_PARAM_LANGUAGE = "language";
    private static final String QUERY_PARAM_PAGE = "page";

    //JSON Key Names
    private static final String KEY_ID = "id";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_POSTER_PATH = "poster_path";

    //JSON Review Keys
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    //JSON Trailer Keys
    private static final String KEY_KEY = "key";
    private static final String KEY_NAME = "name";

    private static final Uri BASE_CONTENT_URI = Uri.parse(BASE_URL);
    private static final Uri IMAGE_CONTENT_URI = Uri.parse(IMAGE_URL);
    private static final Uri VIDEO_URI = Uri.parse(VIDEO_URL);

    public static String getMovieUrl(String path)
    {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        uriBuilder.appendEncodedPath(path);
        uriBuilder.appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY);
        uriBuilder.appendQueryParameter(QUERY_PARAM_LANGUAGE, LANGUAGE);
        uriBuilder.appendQueryParameter(QUERY_PARAM_PAGE, PAGE);
        return uriBuilder.toString();
    }

    public static String getMovieDetailUrl(String id)
    {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        uriBuilder.appendEncodedPath(id);
        uriBuilder.appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY);
        uriBuilder.appendQueryParameter(QUERY_PARAM_LANGUAGE, LANGUAGE);
        return uriBuilder.toString();
    }

    public static String getMovieReviewUrl(String id)
    {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        uriBuilder.appendPath(id);
        uriBuilder.appendEncodedPath(PATH_REVIEW);
        uriBuilder.appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY);
        uriBuilder.appendQueryParameter(QUERY_PARAM_LANGUAGE, LANGUAGE);
        uriBuilder.appendQueryParameter(QUERY_PARAM_PAGE, PAGE);
        return uriBuilder.toString();
    }

    public static String getMovieTrailerUrl(String id)
    {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        uriBuilder.appendPath(id);
        uriBuilder.appendEncodedPath(PATH_VIDEOS);
        uriBuilder.appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY);
        uriBuilder.appendQueryParameter(QUERY_PARAM_LANGUAGE, LANGUAGE);
        return uriBuilder.toString();
    }

    public static String getMovieInfoUrl(String id)
    {
        Uri.Builder uriBuilder = BASE_CONTENT_URI.buildUpon();
        uriBuilder.appendEncodedPath(id);
        uriBuilder.appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY);
        uriBuilder.appendQueryParameter(QUERY_PARAM_LANGUAGE, LANGUAGE);
        return uriBuilder.toString();
    }

    public static String getVideoUrl(String videoKey)
    {
        Uri.Builder uriBuilder = VIDEO_URI.buildUpon();
        uriBuilder.appendEncodedPath(PATH_WATCH);
        uriBuilder.appendQueryParameter(QUERY_PARAM_VIDEO, videoKey);
        return uriBuilder.toString();
    }

    public static ArrayList<Movie> getMoviesFromServer(String link)
    {
        URL url = getUrl(link);

        String jsonResponse = "";

        try {
            jsonResponse = getHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Movie> movies = getMovieList(jsonResponse);
        return movies;
    }

    public static Movie getMovieDetailsFromServer(String link)
    {
        URL url = getUrl(link);

        String jsonResponse = "";

        try {
            jsonResponse = getHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Movie movie = getMovieInfo(jsonResponse);
        return movie;
    }

    public static ArrayList<Review> getMovieReviewsFromServer(String link)
    {
        URL url = getUrl(link);

        String jsonResponse = "";

        try {
            jsonResponse = getHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Review> reviews = getReviewList(jsonResponse);
        return reviews;
    }

    public static ArrayList<Trailer> getMovieTrailersFromServer(String link)
    {
        URL url = getUrl(link);

        String jsonResponse = "";

        try {
            jsonResponse = getHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Trailer> trailers = getTrailerList(jsonResponse);
        return trailers;
    }

    /**
     * Creates a URL Object
     */
    private static URL getUrl(String urlName)
    {
        try {
            URL url = new URL(urlName);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a JSON response from the input stream
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();

        if(inputStream != null)
        {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();

            while(line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Creates HTTP Request
     */
    private static String getHttpRequest(URL url) throws IOException
    {
        String jsonResponse ="";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if(url == null)
            return jsonResponse;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            if(inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    public static ArrayList<Movie> getMovieList(String response)
    {
        if(response.isEmpty())
            return null;

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray results = object.getJSONArray(KEY_RESULTS);

            //Iterate through the list of movies
            for (int i = 0; i < results.length(); i++)
            {
                JSONObject info = results.getJSONObject(i);
                Movie movie = new Movie();

                if(info.has(KEY_ID))
                    movie.setId(info.getInt(KEY_ID));

                //Retrieve relevant movie info
                if(info.has(KEY_TITLE))
                    movie.setTitle(info.getString(KEY_TITLE));

                if(info.has(KEY_POSTER_PATH))
                    movie.setImage(Uri.withAppendedPath(IMAGE_CONTENT_URI, info.getString(KEY_POSTER_PATH)).toString());

                if(info.has(KEY_ORIGINAL_TITLE))
                    movie.setOriginalTitle(info.getString(KEY_ORIGINAL_TITLE));

                if(info.has(KEY_VOTE_AVERAGE))
                    movie.setRating(info.getString(KEY_VOTE_AVERAGE));

                if(info.has(KEY_RELEASE_DATE))
                    movie.setReleaseDate(info.getString(KEY_RELEASE_DATE));

                if(info.has(KEY_OVERVIEW))
                    movie.setOverview(info.getString(KEY_OVERVIEW));

                //Add Movie object to list
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static Movie getMovieInfo(String response)
    {
        try {
            JSONObject info = new JSONObject(response);
            Movie movie = new Movie();

            if(info.has(KEY_ID))
                movie.setId(info.getInt(KEY_ID));

            //Retrieve relevant movie info
            if(info.has(KEY_TITLE))
                movie.setTitle(info.getString(KEY_TITLE));

            if(info.has(KEY_POSTER_PATH))
                movie.setImage(Uri.withAppendedPath(IMAGE_CONTENT_URI, info.getString(KEY_POSTER_PATH)).toString());

            if(info.has(KEY_ORIGINAL_TITLE))
                movie.setOriginalTitle(info.getString(KEY_ORIGINAL_TITLE));

            if(info.has(KEY_VOTE_AVERAGE))
                movie.setRating(info.getString(KEY_VOTE_AVERAGE));

            if(info.has(KEY_RELEASE_DATE))
                movie.setReleaseDate(info.getString(KEY_RELEASE_DATE));

            if(info.has(KEY_OVERVIEW))
                movie.setOverview(info.getString(KEY_OVERVIEW));

            return movie;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Review> getReviewList(String response)
    {
        if(response.isEmpty())
            return null;

        ArrayList<Review> reviews = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray results = obj.getJSONArray(KEY_RESULTS);

            for(int i = 0; i < results.length(); i++)
            {
                JSONObject info = results.getJSONObject(i);
                Review review = new Review();

                if(info.has(KEY_AUTHOR))
                    review.setAuthor(info.getString(KEY_AUTHOR));

                if(info.has(KEY_CONTENT))
                    review.setContent(info.getString(KEY_CONTENT));

                reviews.add(review);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static ArrayList<Trailer> getTrailerList(String response)
    {
        if(response.isEmpty())
            return null;

        ArrayList<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            JSONArray results = object.getJSONArray(KEY_RESULTS);

            for(int i = 0; i < results.length(); i++)
            {
                JSONObject info = results.getJSONObject(i);
                Trailer trailer = new Trailer();

                if(info.has(KEY_NAME))
                    trailer.setName(info.getString(KEY_NAME));

                if(info.has(KEY_KEY))
                    trailer.setKey(info.getString(KEY_KEY));

                trailers.add(trailer);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }
}
