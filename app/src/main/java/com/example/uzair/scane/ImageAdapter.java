package com.example.uzair.scane;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by uzair on 6/7/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<ImageDetails> list;
    private Context context;
    private SparseBooleanArray selectedItems;



    public ImageAdapter(List<ImageDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_details, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        ImageDetails i = list.get(position);
        holder.name.setText(i.getName().length()>15?
                i.getName().substring(0, 12) + "...":
                        i.getName());
        holder.size.setText(i.getSize());
        holder.created.setText(i.getCreated());
        if(i.getUri().toString().indexOf("pdf")==-1)
        Picasso.get().load(i.getUri()).fit().into(holder.image);

        if(i.isChecked())
            holder.viewForeground.setBackgroundColor(context.getResources().getColor(R.color.SelectedListItem));
        else
            holder.viewForeground.setBackgroundColor(context.getResources().getColor(R.color.ListItemBackGroung));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public final TextView size, name, created;
        public final ImageView image;
        public final RelativeLayout viewForeground, viewBackground;

        public ImageViewHolder(View view) {
            super(view);
            created = (TextView) view.findViewById(R.id.created);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            image = view.findViewById(R.id.thumb_nale);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);


        }

    }

    public void removeItem(int position) {
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ImageDetails item, int position) {
        list.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }



}