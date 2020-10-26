package com.example.ppnd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ppnd.R;

import java.util.ArrayList;

public class VolunteerImgAdapter extends RecyclerView.Adapter<VolunteerImgAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    private Context context;
    public static ImageView imageView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.volunteer_img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VolunteerImgAdapter(ArrayList<String> myDataset,Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VolunteerImgAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_volunteerimg, parent, false);
        // set the view's size, margins, paddings and layout parameters...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Glide.with(context).load("http://tomcat.comstering.synology.me/PPND_Server/upload/" + mDataset.get(position)).placeholder(R.drawable.loadingimg).into(imageView);

        // Picasso.with(context).load("http://tomcat.comstering.synology.me/PPND_Server/upload/" + mDataset.get(position)).into(imageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}