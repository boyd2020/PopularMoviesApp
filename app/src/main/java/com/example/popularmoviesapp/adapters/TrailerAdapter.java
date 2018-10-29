package com.example.popularmoviesapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Trailer;
import com.example.popularmoviesapp.utils.MovieUtil;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    //Objects
    private Context context;
    private ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trailer_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        Trailer t = trailers.get(position);
        holder.trailerName.setText(t.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setCursor(List<Trailer> t)
    {
        trailers.clear();
        trailers.addAll(t);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView trailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.trailerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trailer t = trailers.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(MovieUtil.getVideoUrl(t.getKey())));
                    context.startActivity(intent);
                }
            });
        }
    }
}
