package com.example.popularmoviesapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    //Objects
    private Context context;
    private ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.review_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewerName.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setCursor(List<Review> r)
    {
        reviews.clear();
        reviews.addAll(r);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView reviewContent, reviewerName;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.reviewContent);
            reviewerName = itemView.findViewById(R.id.reviewerName);
        }
    }
}
